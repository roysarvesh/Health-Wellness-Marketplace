package com.wellness.backend.integration.openfda;

import java.util.List;

public class OpenFdaResponse {

    private Meta meta;
    private List<Result> results;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Meta {
        private Results results;

        public Results getResults() {
            return results;
        }

        public void setResults(Results results) {
            this.results = results;
        }

        public static class Results {
            private int total;
            private int skip;
            private int limit;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getSkip() {
                return skip;
            }

            public void setSkip(int skip) {
                this.skip = skip;
            }

            public int getLimit() {
                return limit;
            }

            public void setLimit(int limit) {
                this.limit = limit;
            }
        }
    }

    public static class Result {

        private List<String> indications_and_usage;
        private List<String> warnings;
        private OpenFda openfda;

        public List<String> getIndications_and_usage() {
            return indications_and_usage;
        }

        public void setIndications_and_usage(List<String> indications_and_usage) {
            this.indications_and_usage = indications_and_usage;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        public void setWarnings(List<String> warnings) {
            this.warnings = warnings;
        }

        public OpenFda getOpenfda() {
            return openfda;
        }

        public void setOpenfda(OpenFda openfda) {
            this.openfda = openfda;
        }
    }

    public static class OpenFda {
        private List<String> generic_name;

        public List<String> getGeneric_name() {
            return generic_name;
        }

        public void setGeneric_name(List<String> generic_name) {
            this.generic_name = generic_name;
        }
    }
}
