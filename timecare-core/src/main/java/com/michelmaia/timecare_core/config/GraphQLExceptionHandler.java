package com.michelmaia.timecare_core.config;

import com.michelmaia.timecare_core.exception.AccessDeniedGraphQLException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;


@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof AccessDeniedGraphQLException ade) {
            return ade.toGraphQLError(env);
        } else if (ex instanceof AccessDeniedException) {
            return GraphqlErrorBuilder.newError(env)
                    .message("Access Denied")
                    .errorType(ErrorType.FORBIDDEN)
                    .build();
        }
        return null;
    }
}
