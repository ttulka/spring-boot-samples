package com.ttulka.sample.postprocessing;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class PostProcessingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostProcessingDemoApplication.class, args);
    }

    @Component
    static class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            System.out.println(this.getClass().getSimpleName());
        }
    }

    @Component
    static class MyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            System.out.println(this.getClass().getSimpleName() + " postProcessBeforeInitialization " + beanName);
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			System.out.println(this.getClass().getSimpleName() + " postProcessAfterInitialization " + beanName);
        	return bean;
        }
    }

	@Component
	static class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {
		@Override
		public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
			System.out.println(this.getClass().getSimpleName());
		}
	}
}
