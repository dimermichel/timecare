package com.michelmaia.timecare_core.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

public class AccessDeniedGraphQLException extends RuntimeException{

    public AccessDeniedGraphQLException(String message) {
        super(message);
    }

    public GraphQLError toGraphQLError(DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message(getMessage())
                .build();
    }
}
