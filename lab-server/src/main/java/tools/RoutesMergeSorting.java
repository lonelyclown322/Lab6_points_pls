package tools;

import smyts.lab6.common.entities.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RoutesMergeSorting {

    public static void sort(List<Route> routes) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<Route> helper = new LinkedList<>(routes);
        forkJoinPool.invoke(new MergeSortTask(routes, helper, 0, routes.size() - 1));
    }


    private static class MergeSortTask extends RecursiveAction {
        private final List<Route> routes;
        private final List<Route> helper;
        private final int lowPointer;
        private final int highPointer;

        public MergeSortTask(List<Route> routes, List<Route> helper, int lo, int hi) {
            this.routes = routes;
            this.helper = helper;
            this.lowPointer = lo;
            this.highPointer = hi;
        }

        @Override
        protected void compute() {
            if (lowPointer >= highPointer) return;
            int mid = lowPointer + (highPointer - lowPointer) / 2;
            MergeSortTask left = new MergeSortTask(routes, helper, lowPointer, mid);
            MergeSortTask right = new MergeSortTask(routes, helper, mid + 1, highPointer);
            invokeAll(left, right);
            merge(this.routes, this.helper, this.lowPointer, mid, this.highPointer);

        }

        private void merge(List<Route> routes, List<Route> helper, int lowPointer, int mid, int highPointer) {
            for (int i = lowPointer; i <= highPointer; i++) {
                helper.set(i, routes.get(i));
            }
            int i = lowPointer, j = mid + 1;
            for (int k = lowPointer; k <= highPointer; k++) {
                if (i > mid) {
                    routes.set(k, helper.get(j++));
                } else if (j > highPointer) {
                    routes.set(k, helper.get(i++));
                } else if (isLess(helper.get(i), helper.get(j))) {
                    routes.set(k, helper.get(i++));
                } else {
                    routes.set(k, helper.get(j++));
                }
            }
        }

        private boolean isLess(Route a, Route b) {
            return a.compareTo(b) < 0;
        }
    }

}