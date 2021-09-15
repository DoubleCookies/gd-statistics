import './App.css';
import i18n from './i18n';
import { Trans, useTranslation } from 'react-i18next';
import GithubIcon from './GithubIcon';

function App() {
    const {t} = useTranslation();
    const changeLanguage = () => {
        let lng = 'en';
        if (i18n.language === 'en') {
            lng = 'ru';
        }
        i18n.changeLanguage(lng).then();
    }

    return (
        <div className="app">
            <header className="app-header">
                <div style={{marginRight: 16}}>GDStatistics!</div>
                <div>
                    \( ◕  ᗜ  ◕ )/
                    <button className="lang-button" aria-label="Switch language (en/ru)"
                            title="Switch language (en/ru)" onClick={() => changeLanguage()}>
                        🌎
                    </button>
                </div>
            </header>
            <main className="main-content">
                <h2 className="main-headers">{t("aboutTitle")}</h2>
                <Trans i18nKey="aboutText">
                    Этот проект посвящён сбору статистики по уровням в Geometry Dash — сколько раз уровень был скачан, лайкнут,
                    кем он был создан, какой трек использовался и т. д. После этого все результаты заносятся в различные
                    таблицы... и всё!
                </Trans>
                <h2 className="main-headers">{t("locationTitle")}</h2>
                <Trans i18nKey="locationText">
                    Посмотреть на все таблицы можно <a href='https://github.com/DoubleCookies/GDStatistics/tree/master/Statistics'>тут</a>.
                </Trans>
                <h2 className="main-headers">{t("workTitle")}</h2>
                <Trans i18nKey="workList">
                    Всё довольно просто и последовательно:
                    <ul className="tight-list">
                        <li>Обращаемся к серверам Geometry Dash и получаем страницу (10 featured-уровней). После этого
                            продолжаем получать страницы с уровнями до тех пор, пока они не закончатся.
                        </li>
                        <li>На основе списка featured-уровней формируем отдельный список epic-уровней (так как они
                            автоматически являются featured — список будет полным).
                        </li>
                        <li>Всячески манипулируя списками формируем различные новые списки — по сложностям, по кол-ву
                            использований саундтреков, по кол-ву уровней у определенных игроков, и т. д.
                        </li>
                        <li>Сохраняем списки в отдельные файлы.
                        </li>
                        <li><b>Готово!</b></li>
                    </ul>
                </Trans>
                <h2 className="main-headers">{t("plansTitle")}</h2>
                <Trans i18nKey="plansList">
                    <ul className="tight-list">
                        <li>Можно будет пройтись по старым коммитам, собрать информацию по прошлым месяцам/годам
                            и построить сводную табличку/график/<i>другую штуку</i>.
                        </li>
                        <li>Добавить иллюстраций. Все же любят картинки?</li>
                    </ul>
                </Trans>
            </main>
            <footer className="footer-block">
                <div>
                    <GithubIcon />
                </div>
                <div>2018-2021 <span role="img" aria-label="cookieEmoji">🍪</span></div>
            </footer>
        </div>
    );
}

export default App;
