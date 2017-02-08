#ifndef __HGR_DEPENDENCIES_H__
#define __HGR_DEPENDENCIES_H__

#define K1_ERIKA_PLATFORM_MOS
// #include "eecfg.h"
#include "omp.h"
// typedef void* omp_lock_t;
// Not present in omp-lock.h
extern void omp_init_lock(omp_lock_t *lock);
extern void omp_destroy_lock(omp_lock_t *lock);
typedef omp_lock_t hgr_dependency_t;

/* Dependencies */
void hgr_init_dependency(hgr_dependency_t *mutex, const void *attr);
void hgr_destroy_dependency(hgr_dependency_t *mutex);
void hgr_wait_dependency(hgr_dependency_t *mutex);
void hgr_release_dependency(hgr_dependency_t *mutex);

#endif