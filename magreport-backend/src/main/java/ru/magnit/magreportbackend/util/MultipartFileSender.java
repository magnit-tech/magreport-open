package ru.magnit.magreportbackend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MultipartFileSender {
    @Value("${magreport.multipart-file-sender.default-buffer-size}")
    private static int defaultBufferSize = 20400;
    @Value("${magreport.multipart-file-sender.default-expire-time}")
    private static long defaultExpireTime = 6048000;
    @Value("${magreport.multipart-file-sender.multipart-boundary}")
    private String multipartBoundary = "MULTIPART_BYTERANGES";

    @Value("${magreport.multipart-file-sender.count-of-attempts}")
    private int maxCountOfAttempt = 3;

    @Value("${magreport.multipart-file-sender.time-between-of-attempts}")
    private static int timeBetweenAttempts = 500;

    Path filepath;
    String customFileName;
    HttpServletRequest request;
    HttpServletResponse response;

    public MultipartFileSender(String customFileName) {
        this.customFileName = customFileName;
    }

    public MultipartFileSender() {
    }

    public static MultipartFileSender fromPath(Path path, String filename) {
        return new MultipartFileSender(filename).setFilepath(path);
    }

    public static MultipartFileSender fromFile(File file, String filename) {
        return new MultipartFileSender(filename).setFilepath(file.toPath());
    }

    public static MultipartFileSender fromURIString(String uri, String filename) {
        return new MultipartFileSender(filename).setFilepath(Paths.get(uri));
    }

    public static MultipartFileSender fromPath(Path path) {
        return new MultipartFileSender().setFilepath(path);
    }

    public static MultipartFileSender fromFile(File file) {
        return new MultipartFileSender().setFilepath(file.toPath());
    }

    public static MultipartFileSender fromURIString(String uri) {
        return new MultipartFileSender().setFilepath(Paths.get(uri));
    }

    //** internal setter **//
    private MultipartFileSender setFilepath(Path filepath) {
        this.filepath = filepath;
        return this;
    }

    public MultipartFileSender with(HttpServletRequest httpRequest) {
        request = httpRequest;
        return this;
    }

    public MultipartFileSender with(HttpServletResponse httpResponse) {
        response = httpResponse;
        return this;
    }

    public void serveResource() throws Exception {
        if (response == null || request == null) {
            return;
        }

        if (!Files.exists(filepath)) {
            log.error("File doesn't exist at URI : {}", filepath.toAbsolutePath());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }



        long length = Files.size(filepath);
        String fileName = customFileName != null ? customFileName : filepath.getFileName().toString();
        FileTime lastModifiedObj = Files.getLastModifiedTime(filepath);

        if (fileName.isEmpty() || lastModifiedObj == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        long lastModified = LocalDateTime.ofInstant(lastModifiedObj.toInstant(), ZoneId.of(ZoneOffset.systemDefault().getId())).toEpochSecond(ZoneOffset.UTC);


        // If-None-Match header should contain "*" or ETag. If so, then return 304.
        String ifNoneMatch = request.getHeader("If-None-Match");
        if (ifNoneMatch != null && HttpUtils.matches(ifNoneMatch, fileName)) {
            response.setHeader("ETag", fileName); // Required in 304.
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        // If-Modified-Since header should be greater than LastModified. If so, then return 304.
        // This header is ignored if any If-None-Match header is specified.
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
            response.setHeader("ETag", fileName); // Required in 304.
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        // Validate request headers for resume ----------------------------------------------------

        // If-Match header should contain "*" or ETag. If not, then return 412.
        String ifMatch = request.getHeader("If-Match");
        if (ifMatch != null && !HttpUtils.matches(ifMatch, fileName)) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return;
        }

        // If-Unmodified-Since header should be greater than LastModified. If not, then return 412.
        long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
        if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return;
        }

        // Validate and process range -------------------------------------------------------------

        // Prepare some variables. The full Range represents the complete file.
        Range full = new Range(0, length - 1, length);
        List<Range> ranges = new ArrayList<>();

        // Validate and process Range and If-Range headers.
        String range = request.getHeader("Range");
        if (range != null) {

            // Range header should match format "bytes=n-n,n-n,n-n...". If not, then return 416.
            if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
                response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }

            String ifRange = request.getHeader("If-Range");
            if (ifRange != null && !ifRange.equals(fileName)) {
                try {
                    long ifRangeTime = request.getDateHeader("If-Range"); // Throws IAE if invalid.
                    if (ifRangeTime != -1) {
                        ranges.add(full);
                    }
                } catch (IllegalArgumentException ignore) {
                    ranges.add(full);
                }
            }

            // If any valid If-Range header, then process each part of byte range.
            if (ranges.isEmpty()) {
                for (String part : range.substring(6).split(",")) {
                    // Assuming a file with length of 100, the following examples returns bytes at:
                    // 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
                    long start = Range.sublong(part, 0, part.indexOf("-"));
                    long end = Range.sublong(part, part.indexOf("-") + 1, part.length());

                    if (start == -1) {
                        start = length - end;
                        end = length - 1;
                    } else if (end == -1 || end > length - 1) {
                        end = length - 1;
                    }

                    // Check if Range is syntactically valid. If not, then return 416.
                    if (start > end) {
                        response.setHeader("Content-Range", "bytes */" + length); // Required in 416.
                        response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                        return;
                    }

                    // Add range.
                    ranges.add(new Range(start, end, length));
                }
            }
        }
        String disposition = "inline";
        String contentType = "application/vnd.ms-excel.sheet.macroEnabled.12";
        // Initialize response.
        response.reset();
        response.setBufferSize(defaultBufferSize);
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Disposition", disposition + ";filename*=utf-8''" + StringUtils.getUTF8String(fileName) + ".xlsm");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", fileName);
        response.setDateHeader("Last-Modified", lastModified);
        response.setDateHeader("Expires", System.currentTimeMillis() + defaultExpireTime);

        // Send requested file (part(s)) to client ------------------------------------------------

        // Prepare streams.

        var success = false;
        var countAttempt = 0;


        while (!success && countAttempt != maxCountOfAttempt) {
            try {
                InputStream input = new BufferedInputStream(Files.newInputStream(filepath));
                OutputStream output = response.getOutputStream();

                if (ranges.isEmpty() || ranges.get(0) == full) {
                    response.setContentType(contentType);
                    response.setHeader("Content-Range", "bytes " + full.start + "-" + full.end + "/" + full.total);
                    response.setHeader("Content-Length", String.valueOf(full.length));
                    Range.copy(input, output, length, full.start, full.length);

                } else if (ranges.size() == 1) {

                    // Return single part of file.
                    Range r = ranges.get(0);
                    log.info("Return 1 part of file : from ({}) to ({})", r.start, r.end);
                    response.setContentType(contentType);
                    response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
                    response.setHeader("Content-Length", String.valueOf(r.length));
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

                    // Copy single part range.
                    Range.copy(input, output, length, r.start, r.length);

                } else {

                    // Return multiple parts of file.
                    response.setContentType("multipart/byteranges; boundary=" + multipartBoundary);
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

                    // Cast back to ServletOutputStream to get the easy println methods.
                    ServletOutputStream sos = (ServletOutputStream) output;

                    // Copy multi part range.
                    for (Range r : ranges) {
                        log.info("Return multi part of file : from ({}) to ({})", r.start, r.end);
                        // Add multipart boundary and header fields for every range.
                        sos.println();
                        sos.println("--" + multipartBoundary);
                        sos.println("Content-Type: " + contentType);
                        sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);

                        // Copy single part range of multi part range.
                        Range.copy(input, output, length, r.start, r.length);
                    }

                    // End with multipart boundary.
                    sos.println();
                    sos.println("--" + multipartBoundary + "--");
                }
                    success = true;
                    input.close();
                    output.close();

            } catch (Exception ex) {
                if (countAttempt == maxCountOfAttempt) throw new IOException(ex.getMessage(), ex);
                countAttempt++;
                Thread.sleep(timeBetweenAttempts);
            }

        }

    }

    private static class Range {
        long start;
        long end;
        long length;
        long total;

        /**
         * Construct a byte range.
         *
         * @param start Start of the byte range.
         * @param end   End of the byte range.
         * @param total Total length of the byte source.
         */
        public Range(long start, long end, long total) {
            this.start = start;
            this.end = end;
            this.length = end - start + 1;
            this.total = total;
        }

        public static long sublong(String value, int beginIndex, int endIndex) {
            String substring = value.substring(beginIndex, endIndex);
            return (substring.length() > 0) ? Long.parseLong(substring) : -1;
        }

        private static void copy(InputStream input, OutputStream output, long inputSize, long start, long length) throws IOException {
            byte[] buffer = new byte[defaultBufferSize];
            int read;

            if (inputSize == length) {
                // Write full range.
                while ((read = input.read(buffer)) > 0) {
                    output.write(buffer, 0, read);
                    output.flush();
                }
            } else {
                input.skip(start);
                long toRead = length;

                while ((read = input.read(buffer)) > 0) {
                    if ((toRead -= read) > 0) {
                        output.write(buffer, 0, read);
                        output.flush();
                    } else {
                        output.write(buffer, 0, (int) toRead + read);
                        output.flush();
                        break;
                    }
                }
            }
        }
    }

    private static class HttpUtils {

        /**
         * Returns true if the given accept header accepts the given value.
         *
         * @param acceptHeader The accept header.
         * @param toAccept     The value to be accepted.
         * @return True if the given accept header accepts the given value.
         */
        public static boolean accepts(String acceptHeader, String toAccept) {
            String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
            Arrays.sort(acceptValues);

            return Arrays.binarySearch(acceptValues, toAccept) > -1
                    || Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
                    || Arrays.binarySearch(acceptValues, "*/*") > -1;
        }

        /**
         * Returns true if the given match header matches the given value.
         *
         * @param matchHeader The match header.
         * @param toMatch     The value to be matched.
         * @return True if the given match header matches the given value.
         */
        public static boolean matches(String matchHeader, String toMatch) {
            String[] matchValues = matchHeader.split("\\s*,\\s*");
            Arrays.sort(matchValues);
            return Arrays.binarySearch(matchValues, toMatch) > -1
                    || Arrays.binarySearch(matchValues, "*") > -1;
        }
    }
}