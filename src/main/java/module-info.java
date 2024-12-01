module com.tableforge {
    requires transitive javafx.controls;
		requires java.sql;
		requires javafx.base;

		exports com.tableforge;
		exports com.tableforge.models;
		
		opens com.tableforge.models;
}