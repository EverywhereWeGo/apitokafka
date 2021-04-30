#!/bin/sh
script_abs=$(readlink -f "$0")
path=${script_abs%/bin*}

sh $path/bin/stop.sh
sh $path/bin/start.sh $@
