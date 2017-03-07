#ifndef __HGR_DEPENDENCIES_H__
#define __HGR_DEPENDENCIES_H__
#include <ptask.h>

typedef pthread_mutex_t hgr_dependency_t;

/* Dependencies */
void hgr_init_dependency(hgr_dependency_t *mutex, const void *attr);
void hgr_destroy_dependency(hgr_dependency_t *mutex);
void hgr_wait_dependency(hgr_dependency_t *mutex);
void hgr_release_dependency(hgr_dependency_t *mutex);

#endif
