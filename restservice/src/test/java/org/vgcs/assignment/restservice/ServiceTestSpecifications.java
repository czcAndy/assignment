package org.vgcs.assignment.restservice;

import org.junit.jupiter.api.Test;

interface ServiceTestSpecifications {
    void test_getResource_200() throws Exception;
    void test_getResource_400() throws Exception;
    void test_getResource_401() throws Exception;
    void test_getResource_404() throws Exception;
    void test_getResource_500() throws Exception;
    void test_getResource_nullBody() throws Exception;

    void test_getResourceAsync_200() throws Exception;
    void test_getResourceAsync_when_at_least_one_200() throws Exception;
    void test_getResourceAsync_when_none_200() throws Exception;
}
