#!/bin/bash

sudo su - postgres -c "createdb conference_engineer"
echo "CREATE USER ce_webapp WITH PASSWORD 'dIyiADrPADh1UhX9Ecdo'" | sudo su - postgres -c "psql conference_engineer"
cat create_tables.sql | sudo su - postgres -c "psql conference_engineer"
echo "GRANT ALL ON conference_engineer.* TO ce_webapp" | sudo su - postgres -c "psql conference_engineer"
