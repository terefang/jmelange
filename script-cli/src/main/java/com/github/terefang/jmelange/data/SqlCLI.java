package com.github.terefang.jmelange.data;

public class SqlCLI {
}

/*

           <artifactId>jtds</artifactId>
            <artifactId>mysql-connector-java</artifactId>
            <artifactId>ojdbc6</artifactId>
            <artifactId>sqlite-jdbc</artifactId>
            <artifactId>h2</artifactId>
            <artifactId>postgresql</artifactId>
            <artifactId>mariadb-java-client</artifactId>
            <artifactId>csvjdbc</artifactId>
            <artifactId>mssql-jdbc</artifactId>
            <artifactId>duckdb_jdbc</artifactId>

### flyway/liquibase
- Info: Prints current status/version of a database schema. It prints which migrations are pending, which migrations have been applied, the status of applied migrations, and when they were applied.
- Migrate: Migrates a database schema to the current version. It scans the classpath for available migrations and applies pending migrations.
- Baseline: Baselines an existing database, excluding all migrations, including baselineVersion. Baseline helps to start with Flyway in an existing database. Newer migrations can then be applied normally.
- Validate: Validates current database schema against available migrations.
- Repair: Repairs metadata table.
- Clean: Drops all objects in a configured schema. Of course, we should never use clean on any production database.
- Undo:

### $ usql --help
usql, the universal command-line interface for SQL databases

Usage:
  usql [flags]... [DSN]

Arguments:
  DSN   database url or connection name

Flags:
  -c, --command COMMAND                     run only single command (SQL or internal) and exit
  -f, --file FILE                           execute commands from file and exit
  -w, --no-password                         never prompt for password
  -X, --no-init                             do not execute initialization scripts (aliases: --no-rc --no-psqlrc --no-usqlrc)
  -o, --out FILE                            output file
  -W, --password                            force password prompt (should happen automatically)
  -1, --single-transaction                  execute as a single transaction (if non-interactive)
  -v, --set NAME=VALUE                      set variable NAME to VALUE (see \set command, aliases: --var --variable)
  -N, --cset NAME=DSN                       set named connection NAME to DSN (see \cset command)
  -P, --pset VAR=ARG                        set printing option VAR to ARG (see \pset command)
  -F, --field-separator FIELD-SEPARATOR     field separator for unaligned and CSV output (default "|" and ",")
  -R, --record-separator RECORD-SEPARATOR   record separator for unaligned and CSV output (default \n)
  -T, --table-attr TABLE-ATTR               set HTML table tag attributes (e.g., width, border)
  -A, --no-align                            unaligned table output mode
  -H, --html                                HTML table output mode
  -t, --tuples-only                         print rows only
  -x, --expanded                            turn on expanded table output
  -z, --field-separator-zero                set field separator for unaligned and CSV output to zero byte
  -0, --record-separator-zero               set record separator for unaligned and CSV output to zero byte
  -J, --json                                JSON output mode
  -C, --csv                                 CSV output mode
  -G, --vertical                            vertical output mode
  -q, --quiet                               run quietly (no messages, only query output)
      --config string                       config file
  -V, --version                             output version information, then exit
  -?, --help                                show this help, then exit

  https://github.com/xo/usql

 */