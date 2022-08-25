import DateFnsUtils from '@date-io/date-fns';
import format from "date-fns/format";
import ruLocale from "date-fns/locale/ru";

export default class RuLocalizedUtils extends DateFnsUtils {
    getCalendarHeaderText(date) {
        return format(date, "LLLL yyyy", { locale: ruLocale });
    }
    getDatePickerHeaderText(date) {
        return format(date, "dd MMMM", { locale: ruLocale });
    }
    getYearText(date) {
        return format(date, "yyyy", { locale: ruLocale });
    }
    getMonthText(date) {
        return format(date, "LLLL", { locale: ruLocale });
    }
}