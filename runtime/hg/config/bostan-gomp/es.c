#include <stdio.h>

int main (int argc, char **argv)
{
	int a[1], b[1], c[1], d[1];
	// int d0[1], d1, d2, d3;

	GOMP_init(0 /* device id */);

	printf("==== EXECUTING ON COMPUTATION CLUSTERS ====\n");
	
	#pragma omp target map(to:a[0:1],d[0:1]) map(from:b[0:1],c[0:1]) //nowait
	{
		printf("Inside target\n");
		#pragma omp parallel
		{
			printf("Inside parreg\n");
			#pragma omp single
			{
				printf("Inside single\n");
				#pragma omp task shared(a, b) depend(out:b, c) depend(in:d)
				{
					printf("Inside task #0\n");
					b[0] = a[0] + 1;
					c[0] = d[0];
					
				}
				
				#pragma omp task shared(a, b) depend(in:b)
				{
					printf("Inside task #1\n");
					a[0] = b[0] + 1;
				}
			}
		}
	} // target
	#pragma omp target map(to:a[0:1]) map(from:b[0:1]) //nowait
	{
		printf("Target #2\n");
	}// target #2
	
	printf("\n--------------------------------------\n\n");
	
	GOMP_deinit(0);

	return 0;
}
