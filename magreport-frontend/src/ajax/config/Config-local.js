export default function Config(){
    this.PROTOCOL = 'https:';
    this.HOST = 'localhost';
    this.API_BASE_URL = '/api/v1';

    this.BASE_URL = this.PROTOCOL + "//" + this.HOST + (this.PORT ? ":" + this.PORT : "") + this.API_BASE_URL;
    this.LOGIN_URL = this.PROTOCOL + "//" + this.HOST + (this.PORT ? ":" + this.PORT : "") + "/login";
}