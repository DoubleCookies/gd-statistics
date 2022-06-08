import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import detector from 'i18next-browser-languagedetector';

const resources = {
    en: {
        translation: {
            aboutTitle: "What is it?",
            aboutText: "This project is about collecting data about levels in Geometry Dash — how many times level was downloaded, liked, " +
                " who is creator, which soundtrack it uses, etc. After that different tables are filled with data.. and that's all!",
            locationTitle: "Where can I find it?",
            locationText: "You can check data <1>here</1>.",

            workTitle: "How does it work?",
            workList: "Everything is pretty simple and consecutive:<1>" +
                "<0>Fetch data from Geometry Dash servers and get page (10 featured levels). We are getting data until we found " +
                "last featured level.</0>" +
                "<0>Form list for epic levels (because every epic level is featured we won't miss anything)</0>" +
                "<0>Both lists split for another two lists  — for all levels and for demon-only.</0>" +
                "<0>By manipulating the lists, we form various new lists - by difficulty, by soundtracks usage, by " +
                "amount of levels from creator, etc.</0>" +
                "<0>Save data in different files.</0>" +
                "<0><2>Done!</2></0>" +
                "</1>",
            plansTitle: "Any plans?",
            plansList: "<0>" +
                "<1>Maybe check old commits, collect data from old months/years and " +
                "build graph/table/<2>other thing</2>.</1>" +
                "<1>Add illustrations. Everybody likes illustrations I guess?</1>" +
                "</0>",
        }
    },
    ru: {
        translation: {
            aboutTitle: "Что это такое?",
            aboutText: "Этот проект посвящён сбору статистики по уровням в Geometry Dash — сколько раз " +
                "уровень был скачан, лайкнут, кем он был создан, какой трек использовался и т. д. " +
                "После этого все результаты заносятся в различные " +
                "таблицы.. и всё!",

            locationTitle: "Где можно найти?",
            locationText: "Посмотреть на данные можно <1>тут</1>.",

            workTitle: "Как он работает?",
            workList: "Всё довольно просто и последовательно:<1>" +
                "<0>Обращаемся к серверам Geometry Dash и получаем страницу (10 featured-уровней). После этого " +
                "продолжаем получать страницы с уровнями до тех пор, пока они не закончатся.</0>" +
                "<0>На основе списка featured-уровней формируем отдельный список epic-уровней (так как они " +
                "автоматически являются featured — мы ничего не упустим).</0>" +
                "<0>Манипулируя списками формируем различные новые списки — по сложностям, по кол-ву " +
                "использований саундтреков, по кол-ву уровней у определенных игроков, и т. д.</0>" +
                "<0>Сохраняем списки в отдельные файлы.</0>" +
                "<0><2>Готово!</2></0>" +
                "</1>",
            plansTitle: "Есть ли планы на будущее?",
            plansList: "<0>" +
                "<1>Можно будет пройтись по старым коммитам, собрать информацию по прошлым месяцам/годам " +
                "и построить сводную табличку/график/<2>другую штуку</2>.</1>" +
                "<1>Добавить иллюстраций. Все же любят картинки?</1>" +
                "</0>",
        }
    }
}

i18n.use(detector).use(initReactI18next).init({
    fallbackLng: 'en',
    resources,
})

export default i18n;