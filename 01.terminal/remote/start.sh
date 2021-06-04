#!/bin/sh
echo 开始启动程序
script_abs=$(readlink -f "$0")
path=${script_abs%/bin*}

cd $path && java -Xms1024m -Xmx1024m -jar ${project.name}-1.0-SNAPSHOT.jar $@ 1>$path/log.log 2>$path/log.log &


echo 开始启动监控脚本
#启动监控脚本
checkrunpid=$(ps -ef | grep checkrun.sh | grep -v grep | awk '{print $2}')
kill -9 ${checkrunpid}


script_abs=$(readlink -f "$0")
path=${script_abs%/bin*}

sh $path/bin/checkrun.sh $@ 1>$path/bin/checkrun.log 2>$path/bin/checkrun.log &
