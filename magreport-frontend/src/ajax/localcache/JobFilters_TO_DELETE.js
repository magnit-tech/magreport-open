function JobFilters(startDate, endDate, username, jobStatuses, updatePeriod, interval, cardsOnPage){
    this.startDate = startDate;
    this.endDate = endDate;
    this.username = username;
    this.jobStatuses = jobStatuses;
    this.updatePeriod = updatePeriod;
    this.interval = interval;
    this.cardsOnPage = cardsOnPage;
}
export default JobFilters;