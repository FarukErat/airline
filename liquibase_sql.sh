#!/bin/bash

set -euo pipefail

mvn liquibase:updateSQL

cp ./target/liquibase/migrate.sql migrate.sql
