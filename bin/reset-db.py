#!/usr/bin/python
# -*- coding: utf-8 -*-

import psycopg2
from sys import exit
import os

# where we are in the file
STATE_NONE = 0
STATE_UPS = 1
STATE_DOWNS = 2
state = STATE_NONE
# the scripts to execute
ups = ""
downs = ""
evolutions_path = "./conf/evolutions/default/1.sql"
# markers that indicate state change from ups to downs
UPS_MARKER = "# --- !Ups"
DOWNS_MARKER = "# --- !Downs"

if not os.path.exists(evolutions_path):
    print('could not find evolutions script at: %s' % evolutions_path)
    exit(1)

with open(evolutions_path) as f:
    for line in f.readlines():
        # handle markers
        if line.strip().startswith(UPS_MARKER):
            state = STATE_UPS
            continue
        if line.strip().startswith(DOWNS_MARKER):
            state = STATE_DOWNS
            continue

        if state is STATE_UPS:
            ups += line
        if state is STATE_DOWNS:
            downs += line

try:
    with psycopg2.connect(database='hikerplaces', user='postgres') as con:
        with con.cursor() as cur:
            print('executing downs...')
            cur.execute(downs)
            print('executing ups...')
            cur.execute(ups)
    

except psycopg2.DatabaseError as e:
    print('Error %s' % e)
    exit(1)
