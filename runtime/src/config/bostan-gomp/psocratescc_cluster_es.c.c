struct gomp_ol_args_0_t;
int printf(const char *, ...) __attribute__((__format__(__printf__, 1, 2)));
struct  gomp_ol_args_0_t
{
  int **a;
  int **b;
};
static void _ol__cluster_fn_0_0(struct gomp_ol_args_0_t *ol_data_0)
{
  int *const *const b = &(*(*ol_data_0).b);
  int *const *const a = &(*(*ol_data_0).a);
  {
    printf("Inside task\n");
    (*b)[0] = (*a)[0] + 1;
  }
  ;
}
struct gomp_ol_args_1_t;
extern _Bool GOMP_single_start(void);
struct  gomp_ol_args_1_t
{
  int **b;
  int **a;
};
extern void GOMP_task(void (*)(void *), void *, void (*)(void *, void *), long int, long int, _Bool, unsigned int, void **, int);
extern void GOMP_barrier(void);
static void _ol__cluster_fn_0_1(struct gomp_ol_args_1_t *ol_data_1)
{
  int *const *const a = &(*(*ol_data_1).a);
  int *const *const b = &(*(*ol_data_1).b);
  {
    if (GOMP_single_start())
      {
        /* << fake context >> { */
        struct gomp_ol_args_0_t ol_data_0;
        ol_data_0.a = (int *const *) a;
        ol_data_0.b = (int *const *) b;
        unsigned long int gomp_task_flags_0 = 0;
        GOMP_task((void (*)(void *))_ol__cluster_fn_0_0, &ol_data_0, (void (*)(void *, void *))0, 8, 4, 1, gomp_task_flags_0, 0, 1U);
        /* } << fake context >> */
      }
    GOMP_barrier();
  }
  ;
}
struct _omp_param;
typedef struct _omp_param omp_param_t;
extern void GOMP_set_tdg_id(unsigned int tdg_id);
struct  _omp_param
{
  void *ptr;
  unsigned long int size;
  char type;
};
extern void GOMP_parallel_start(void (*)(void *), void *, unsigned int);
extern void GOMP_parallel_end(void);
extern void GOMP_unset_tdg_id(void);
static void _cluster_fn_0(omp_param_t *_params)
{
  GOMP_set_tdg_id(0U);
  int *const a = *((int (*)[1])_params[0].ptr);
  int *const b = *((int (*)[1])_params[1].ptr);
  {
    struct gomp_ol_args_1_t ol_data_1;
    ol_data_1.b = &b;
    ol_data_1.a = &a;
    GOMP_parallel_start((void (*)(void *))_ol__cluster_fn_0_1, &ol_data_1, 0);
    _ol__cluster_fn_0_1(&ol_data_1);
    GOMP_parallel_end();
  }
  GOMP_unset_tdg_id();
}
void (*omp_functions[1])(omp_param_t *) = {[0] = _cluster_fn_0};
int main(int argc, char **argv)
{
  ;
}
