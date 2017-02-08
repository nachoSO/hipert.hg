MAINSRCS = main.c
CSRCS = syntethicTask1.c syntethicTask2.c

CFLAGS= -DTOTAL_TASK=2 -O0 -g3
LDFLAGS=

DOTSRCS= èdef.dot ciao.dot

#BACKEND=./config/pthread_runtime
BACKEND=./config/bostan-gomp

SCHEDULE=global

include $(BACKEND)/Makefile.hgr
