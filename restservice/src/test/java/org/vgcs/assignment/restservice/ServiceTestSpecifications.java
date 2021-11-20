package org.vgcs.assignment.restservice;

interface ServiceTestSpecifications {
    void test_getResource_200() throws Exception;
    void test_getResource_400() throws Exception;
    void test_getResource_401() throws Exception;
    void test_getResource_404() throws Exception;
    void test_getResource_500() throws Exception;
    void test_getResource_nullBody() throws Exception;

    void test_getResourceAsync_200();
    void test_getResourceAsync_400();
    void test_getResourceAsync_401();
    void test_getResourceAsync_404();
    void test_getResourceAsync_500();
}
