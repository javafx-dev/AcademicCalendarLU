package academiccalendar.ui.main;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class AbstractJavaFxApplicationSupport extends Application {

    private static String[] savedArgs;

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        applicationContext = SpringApplication.run(getClass(), savedArgs);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void stop() throws Exception {

        super.stop();
        applicationContext.close();
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    protected static void launchApp(Class<? extends AbstractJavaFxApplicationSupport> appClass, String[] args) {

        AbstractJavaFxApplicationSupport.savedArgs = args;
        Application.launch(appClass, args);
    }
}
