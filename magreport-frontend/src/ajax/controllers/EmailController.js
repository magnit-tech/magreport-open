const CONTROLLER_URL = '/email';
const METHOD = 'POST';

const EMAIL_SEND = CONTROLLER_URL + '/send';
const EMAIL_CHECK = CONTROLLER_URL + "/check"


export default function EmailController(dataHub) {

    this.send = function (subject, bodyText, TO, CC, BCC, callback) {
        const body = {
            subject: subject,
            body: bodyText,
            to: TO,
            cc: CC,
            bcc: BCC
        };
        return dataHub.requestService(EMAIL_SEND, METHOD, body, callback);
    }

    this.check = function (emails, callback) {
        const body = {
            emails: emails
        };

        return dataHub.requestService(EMAIL_CHECK, METHOD, body, callback)
    }

}

