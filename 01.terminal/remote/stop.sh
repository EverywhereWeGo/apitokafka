#!/bin/sh
echo 开始关闭程序
pid=`ps -ef | grep ${project.name}-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $2}'`
echo $pid
kill -9 $pid

echo 开始关闭监控程序
checkrunpid=$(ps -ef | grep checkrun.sh | grep -v grep | awk '{print $2}')
kill -9 ${checkrunpid}