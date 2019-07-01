package org.stefata.mediadownloader;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@Sql(scripts = "classpath:cleanup.sql")
public class DatabaseIT {
}
