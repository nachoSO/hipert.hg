#include <stdlib.h>
#include <stdio.h>

#include "libpsocoffload.h"
#include "_hgr.h"

static void _host_fn_0(omp_param_t *_params)
{
	printf("FATAL ERROR\n");
}

struct mppa_target_param_0;
struct mppa_target_param_0
{
  int n;
  omp_param_t params[2];
};

int main(int argc, char **argv)
{
  int b[1];
  int a[1];
  GOMP_init(0);
	struct mppa_target_param_0 *mppa_data_1;
	/* Offloaded data structure is freed in the Runtime. */ 
	mppa_data_1 = malloc(sizeof(struct mppa_target_param_0));
	(*mppa_data_1).n = 2;
	(*mppa_data_1).params[0].ptr = a;
	(*mppa_data_1).params[0].size = (4) * (1);
	(*mppa_data_1).params[0].type = 0;
	(*mppa_data_1).params[1].ptr = b;
	(*mppa_data_1).params[1].size = (4) * (1);
	(*mppa_data_1).params[1].type = 1;
  log("Init CC\n");
	GOMP_target(0, (void (*)(void *))_host_fn_0, (void *)0, (void *)mppa_data_1, 0, 65535U);
	GOMP_target_wait(0, (void *)mppa_data_1, 0);
  
	log("Done. Now, Invoking gen_main()\n");
	int ret = gen_main(0,0x0);
	log("gen_main() returned %d...\n", ret);
	
  GOMP_deinit(0);
  return 0;
}
