# Flyway Fixed Java Migrations ignore Locations settings

The property `spring.flyway.locations` sets the location of resources for Flyway migrations.

This is ignored for Fixed Java Migrations, when they come via `FlywayAutoConfiguration`.

`FlywayAutoConfiguration` sets all Spring Beans from the current Application Context into the Flyway Configuration regardless of the given Locations settings.

## Demo Application

There are two Java packages with Flyway Java Migration classes:
- `db.javamigration1.h2`
- `db.javamigration2.h2`

The former contains a Spring Component, the latter not.

Setting the property `spring.flyway.locations` to `classpath:db/javamigration1/{vendor}` will result to execution of only Migrations from the package `db.javamigration1.h2` - that's correct.     

On the other hand, setting the property `spring.flyway.locations` to `classpath:db/javamigration1/{vendor}` will result to execution of only Migrations from both packages `db.javamigration1.h2` and `db.javamigration2.h2` as well - that's incorrect.