package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/${env}.properties")
public interface WebDriverConfig extends Config {
    @Key("browser.name")
    @DefaultValue("chrome")
    String browserName();

    @Key("browser.version")
    String browserVersion();

    @Key("browser.size")
    String browserSize();

    @Key("browser.remote.url")
    String browserRemoteUrl();

    @Key("page.load.strategy")
    @DefaultValue("eager")
    String pageLoadStrategy();
}
