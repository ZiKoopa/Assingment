public class PaidAppInfo {
        private int count;
        private int totalDownloads;

        public PaidAppInfo(int count, int totalDownloads) {
            this.count = count;
            this.totalDownloads = totalDownloads;
        }

        public int getCount() {
            return count;
        }

        public int getTotalDownloads() {
            return totalDownloads;
        }

        public PaidAppInfo increment(int downloads) {
            return new PaidAppInfo(count + 1, totalDownloads + downloads);
        }
    }


