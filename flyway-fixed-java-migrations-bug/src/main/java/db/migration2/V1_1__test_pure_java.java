package db.migration2;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class V1_1__test_pure_java extends BaseJavaMigration {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void migrate(Context context) {
        System.out.println("V1_1__test_pure_java: applicationContext.noNull == " + (applicationContext != null));
    }
}
