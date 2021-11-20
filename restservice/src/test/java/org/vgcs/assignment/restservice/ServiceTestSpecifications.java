package org.vgcs.assignment.restservice;

import org.junit.jupiter.api.Test;
import org.vgcs.assignment.restservice.exception.RestCallException;

interface ServiceTestSpecifications {
    @Test void test_getResource_200();
    @Test void test_getResource_400() throws RestCallException;
    @Test void test_getResource_401() throws RestCallException;
    @Test void test_getResource_404() throws RestCallException;
    @Test void test_getResource_500() throws RestCallException;
    @Test void test_getResource_nullBody() throws RestCallException;

    @Test void test_getResourceAsync_200();
    @Test void test_getResourceAsync_when_at_least_one_200();
    @Test void test_getResourceAsync_when_none_200();
}
