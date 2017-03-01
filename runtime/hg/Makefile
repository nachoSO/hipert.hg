MAINSRCS = main.c
CSRCS = syntethicTask1.c

CFLAGS= -DTOTAL_TASK=1 -O0 -g3
LDFLAGS=

DOTSRCS= def.dot ciao.dot

#BACKEND=./config/pthread_runtime
BACKEND=./config/bostan-gomp

SCHEDULE=global

include $(BACKEND)/Makefile.hgr
