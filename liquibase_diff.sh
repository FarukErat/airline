#!/bin/bash

set -euo pipefail

mvn liquibase:diff
