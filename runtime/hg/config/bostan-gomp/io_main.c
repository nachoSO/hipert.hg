struct  _gomp_context
{
  char io_sync[20];
  int io_sync_fd;
  char command_portal[20];
  int command_portal_fd;
  char input_data_portal[2][20];
  int input_data_portal_fd[2];
  char output_data_portal[2][20];
  int output_data_portal_fd[2];
};
struct _gomp_context ctx[16];
struct _omp_param;
typedef void (*omp_fn)(struct _omp_param *);
extern omp_fn omp_functions[];
static const int erika_enterprise_version = 0;
struct _reent;
extern struct _reent *_impure_ptr;
extern struct _reent *const _global_impure_ptr;
typedef struct _omp_param omp_param_t;
struct  _omp_param
{
  void *ptr;
  unsigned long int size;
  char type;
};
int printf(const char *, ...) __attribute__((__format__(__printf__, 1, 2)));
static void _host_fn_0(omp_param_t *_params)
{
  int *const a = *((int (*)[1])_params[0].ptr);
  int *const b = *((int (*)[1])_params[1].ptr);
  {
#pragma omp parallel shared(b, a)    
#pragma omp single   
#pragma omp task shared(a, b)   
    {
      printf("Inside task\n");
      b[0] = a[0] + 1;
    }
  }
}
extern void GOMP_init(int device);
struct mppa_target_param_0;
extern void *malloc(unsigned int size);
struct  mppa_target_param_0
{
  int n;
  omp_param_t params[2];
};
extern void GOMP_target(int device, void (*fn)(void *), const void *target_fn, void *data, unsigned int slot_id, unsigned int mask);
extern void GOMP_target_wait(int device, void *data, unsigned int slot_id);
void GOMP_deinit(int device);
int main(int argc, char **argv)
{
  int b[1];
  int a[1];
  GOMP_init(0);
  printf("==== EXECUTING ON COMPUTATION CLUSTERS ====\n");
#pragma omp task shared(b, a)
  {
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
    GOMP_target(0, (void (*)(void *))_host_fn_0, (void *)0, (void *)mppa_data_1, 0, 65535U);
    GOMP_target_wait(0, (void *)mppa_data_1, 0);
  }
  printf("\n--------------------------------------\n\n");
  GOMP_deinit(0);
  return 0;
}
