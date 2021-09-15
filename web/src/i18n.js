import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import detector from 'i18next-browser-languagedetector';

const resources = {
    en: {
        translation: {
            aboutTitle: "What is it?",
            aboutText: "I collect data about top-50 most downloaded and most liked levels in Geometry Dash " +
                "(overall and for \"demon\" difficulty levels). After that I form table and " +
                "post it on Geometry Dash Wiki (ru). At the same time I create data for two templates which are used " +
                "in articles about most downloaded levels.<1/>" +
                "In result there is semi-automatic articles and templates update (and as a bonus, Wiki contains " +
                "changes for these pages).",
            workTitle: "How does it work?",
            workList: "Everything is pretty simple and consecutive:<1>" +
                "<0>Fetch data from Geometry Dash servers and get page (10 levels). Depending on type " +
                "(downloads/likes) form two levels lists.</0>" +
                "<0>Stop processing pages when we have 50 demon levels (automatically " +
                "we will have 50 levels with every difficulty).</0>" +
                "<0>Both lists split for another two lists  — for all levels and for demon-only.</0>" +
                "<0>Transform every list in txt format " +
                "(with nuances like template types, presence or absence of links, dividers, etc.)</0>" +
                "<0>Process text for templates and save it.</0>" +
                "<0><2>Done!</2></0>" +
                "</1>",
            plansTitle: "Any plans?",
            plansList: "<0><1>I can check old commits, collect data from old months/years and " +
                "build graph/table/<2>other thing</2>.</1>" +
                "<1>Add illustrations. Everybody likes illustrations I guess?</1></0>",
        }
    },
    ru: {
        translation: {
            aboutTitle: "Что это такое?",
            aboutText: "Этот проект посвящён сбору статистики по уровням в Geometry Dash — сколько раз " +
                "уровень был скачан, лайкнут, кем он был создан, какой трек использовался и т. д. " +
                "После этого все результаты заносятся в различные " +
                "таблицы... и всё!",

            locationTitle: "Где можно найти?",
            locationText: "Посмотреть на все таблицы можно <1>тут</1>.",

            workTitle: "Как он работает?",
            workList: "Всё довольно просто и последовательно:<1>" +
                "<0>Обращаемся к серверам Geometry Dash и получаем страницу (10 featured-уровней). После этого " +
                "продолжаем получать страницы с уровнями до тех пор, пока они не закончатся.</0>" +
                "<0>На основе списка featured-уровней формируем отдельный список epic-уровней (так как они " +
                "автоматически являются featured — список будет полным).</0>" +
                "<0>Всячески манипулируя списками формируем различные новые списки — по сложностям, по кол-ву " +
                "использований саундтреков, по кол-ву уровней у определенных игроков, и т. д.</0>" +
                "<0>Сохраняем списки в отдельные файлы.</0>" +
                "<0><2>Готово!</2></0>" +
                "</1>",
            plansTitle: "Есть ли планы на будущее?",
            plansList: "<0><1>Можно будет пройтись по старым коммитам, собрать информацию по прошлым месяцам/годам " +
                "и построить сводную табличку/график/<2>другую штуку</2>.</1>" +
                "<1>Добавить иллюстраций. Все же любят картинки?</1></0>",
        }
    }
}

i18n.use(detector).use(initReactI18next).init({
    fallbackLng: 'ru',
    resources,
})

export default i18n;