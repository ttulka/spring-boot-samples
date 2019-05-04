package com.ttulka.sample.msgsource;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.ObjectUtils;

class CompositeMessageSource implements MessageSource {

    private final Iterable<MessageSource> messageSources;

    public CompositeMessageSource(Iterable<MessageSource> messageSources) {
        this.messageSources = messageSources;
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        try {
            return this.getMessage(code, args, locale);
        } catch (NoSuchMessageException ignore) {
        }
        return defaultMessage;
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        for (MessageSource messageSource : this.messageSources) {
            try {
                return messageSource.getMessage(code, args, locale);
            } catch (NoSuchMessageException ignore) {
            }
        }
        throw new NoSuchMessageException(code, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        for (MessageSource messageSource : this.messageSources) {
            try {
                return messageSource.getMessage(resolvable, locale);
            } catch (NoSuchMessageException ignore) {
            }
        }
        throw new NoSuchMessageException(
                !ObjectUtils.isEmpty(resolvable.getCodes()) ? resolvable.getCodes()[resolvable.getCodes().length - 1] : "", locale);
    }
}
