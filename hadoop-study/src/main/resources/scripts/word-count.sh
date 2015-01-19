#!/bin/bash

JAR_FILE="hadoop-study-0.0.1.jar"
JOB_CLASS="WordCount"
HADOOP="$(which hadoop)"

INPUT="/input/README.txt"
OUTPUT="/output/WordCount"

JOB_CMD="${HADOOP} jar ${JAR_FILE} ${JOB_CLASS} ${INPUT} ${OUTPUT}"

CAT_CMD="${HADOOP} fs -cat ${OUTPUT}/part-*"

RMR_CMD="${HADOOP} fs -rmr ${OUTPUT}"

LOG_FILE="wordcount_'date +%s'.txt"
{
	echo ${JOB_CMD}
	${JOP_CMD}

	if [$?-ne 0]
	then
		echo "Job failed"
		echo ${RMR_CMD}
		${RMR_CMD}
		exit $?
	fi
	
	exit 0
} &> ${LOG_FILE}