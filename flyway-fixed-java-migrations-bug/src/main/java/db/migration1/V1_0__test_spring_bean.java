package db.migration1;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class V1_0__test_spring_bean extends BaseJavaMigration {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void migrate(Context context) {
        System.out.println("V1_0__test_spring_bean: applicationContext.noNull == " + (applicationContext != null));
    }
}
