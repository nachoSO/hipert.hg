#export PATH=/home/unimore/psoc_mapper:/home/bsc/psocrates_dependencies_tools/bin:$PATH

## Points to P-SOCRATES SDK
PSOCSDK		= /usr/local/psoctools/
#PSOCSDK         = /home/bsc/psoctools-repo/psoctools/
PSOCSDK_BIN	= ${PSOCSDK}/bin

## Points to Mercurium compiler
CC		= psocratescc
PSOC_CC		= ${PSOCSDK_BIN}/${CC}
PSOC_CC_CLUST   = ${PSOCSDK_BIN}/${CC}-cluster
PSOC_BOXER		= ${PSOCSDK_BIN}/boxer
PSOC_MAPPER		= ${PSOCSDK_BIN}/psoc_mapper

## Application 
APP		= es
APPSRC		= ${APP}.c

## IOS part
IOS_SRC_NAME	= ${CC}_${APPSRC}
IOS_ELF_NAME	= io_elf

## Cluster part
CLUST_SRC_NAME	= ${CC}_cluster_${APPSRC}.c
CLUST_ELF_NAME	= erika
TDG		= psocrates_tdg_${APP}

## Multibinary
MULTIBIN_NAME	= ${IOS_ELF_NAME}.mpk

POW_OUTFILE=./power

all:
## Extract TDG, separate IO and Clust code
# PB note: PAR1 is needed for automating the experiments
	$(PSOC_CC) --v --tdg --debug-flags=tdg_to_json ${PAR1} --target-mppa -c -K ${APPSRC}
	$(PSOC_BOXER) -to ${TDG} json/*.json
	$(PSOC_MAPPER) -o ${TDG}.c ${TDG}_0.dot 

# Compile other IO code
# 	$(PSOC_CC) --v --target-mppa trie.c -c -o trie_io.o
				
## Compile other Clust code
# 	$(PSOC_CC_CLUST) --v --target-mppa trie.c -c -o trie_clust.o
	$(PSOC_CC_CLUST) --v --target-mppa ${TDG}.c -c -o ${TDG}.o
# 	$(PSOC_CC_CLUST) --v --target-mppa gomp.c -c -o gomp.o

## Compile, link and create multibin
	$(PSOC_CC) --v --target-mppa --sublink-output=${CLUST_ELF_NAME}  -o ${IOS_ELF_NAME} es.o \
			 --Wx:psocratescc-cluster:l,${TDG}.o 

run:
# 	ln -svf ${APP}.mpk ${IOS_ELF_NAME}.mpk
	k1-jtag-runner --multibinary ${MULTIBIN_NAME} --exec-multibin=IODDR0:${IOS_ELF_NAME}
	
runpower:
	k1-power --output=${POW_OUTFILE} --gnuplot=pdf --traces_keep --gnuplot_keep -- k1-jtag-runner --multibinary ${MULTIBIN_NAME} --exec-multibin=IODDR0:${IOS_ELF_NAME}

.PHONY: clean
clean:
	rm -f ${CLUST_SRC_NAME} ${IOS_SRC_NAME} cluster_${APPSRC}.c
	rm -fr json dot ${TDG}.dot
	rm -f *~ *.o  ${IOS_ELF_NAME} ${CLUST_ELF_NAME} ${MULTIBIN_NAME} io.* ${IOS_ELF_NAME}.* *.elf ${MULTIBIN_NAME}

