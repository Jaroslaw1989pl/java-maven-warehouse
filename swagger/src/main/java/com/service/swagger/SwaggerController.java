package com.service.swagger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {

    @GetMapping("/index")
    public String index(Model model) {

        List<Object> services = new ArrayList<>();

        try {

            Map<String, Object> service = new HashMap<>();
            List<Object> controllers = new ArrayList<>();

            Class<?> reflectionController = Class.forName("com.service.user.UserController");
            
            service.put("name", reflectionController.getPackageName());
            
            RequestMapping requestMappingAnnotation = reflectionController.getAnnotation(RequestMapping.class);

            Map<String, Object> controller = new HashMap<>();
            List<String> serviceEndpoints = new ArrayList<>(Arrays.asList(requestMappingAnnotation.value()));
            List<Object> endpoints = new ArrayList<>();

            controller.put("name", reflectionController.getSimpleName());

            for (Method method : reflectionController.getDeclaredMethods()) {


                List<Object> parameters = new ArrayList<>();
                List<Object> responses = new ArrayList<>();
                Parameter[] methodParameters = method.getParameters();

                /*** ENDPOINT PARAMETERS - class method parameters ***/

                if (methodParameters.length > 0) {

                    for (Parameter methodParameter : methodParameters) {

                        Map<String, Object> parameter = new HashMap<>();
                        RecordComponent[] recordComponents = methodParameter.getType().getRecordComponents();
                        
                        if (recordComponents != null) {
                            // record objects - DTO
                            
                            JSONObject parameterModel = new JSONObject();

                            for (RecordComponent recordComponent : recordComponents) {
                                parameterModel.put(recordComponent.getName(), recordComponent.getType().getSimpleName().toLowerCase());
                            }

                            parameter.put("name", methodParameter.getName());
                            parameter.put("type", "object");
                            parameter.put("model", parameterModel);
                        } else {
                            // primitive types: String, Int...
                            parameter.put("name", methodParameter.getName());
                            parameter.put("type", methodParameter.getType().getSimpleName().toLowerCase());
                        }

                        try {
                            PathVariable pathVariableAnnotation = methodParameter.getAnnotation(PathVariable.class);

                            parameter.put("required", pathVariableAnnotation.required() ? "true" : "false");
                            parameter.put("source", "path");
                            
                        } catch (NullPointerException exception) {
                            // System.out.println(exception.getMessage());
                        }
                        try {
                            RequestBody requestBodyAnnotation = methodParameter.getAnnotation(RequestBody.class);

                            parameter.put("required", requestBodyAnnotation.required() ? "true" : "false");
                            parameter.put("source", "body");

                        } catch (NullPointerException exception) {
                            // System.out.println(exception.getMessage());
                        }

                        parameters.add(parameter);
                    }
                }

                /*** ENDPOINT RESPONSE - class method return type ***/

                Map<String, Object> response = new HashMap<>();

                response.put("code", 200);
                response.put("desc", "Successful operation");

                Type returnType = method.getGenericReturnType();

                if (returnType instanceof ParameterizedType parameterizedReturnType) {
                    
                    Type[] genericTypes = parameterizedReturnType.getActualTypeArguments();

                    for (Type genericType : genericTypes) {

                        // conditions check if response is a list
                        if (genericType instanceof ParameterizedType returnClass) {
                            
                            Type[] returnClassGenericTypes = returnClass.getActualTypeArguments();

                            try {

                                for (Type returnClassGenericType : returnClassGenericTypes) {
                                    
                                    Class<?> genericReturnClass = Class.forName(returnClassGenericType.getTypeName());
                                    RecordComponent[] genericRecordComponents = genericReturnClass.getRecordComponents();
                                    
                                    if (genericRecordComponents != null) {
                                        // record objects - DTO

                                        JSONObject returnModel = new JSONObject();
    
                                        for (RecordComponent recordComponent : genericRecordComponents) {
                                            returnModel.put(recordComponent.getName(), recordComponent.getType().getSimpleName().toLowerCase());
                                        }
    
                                        response.put("model", Arrays.asList(returnModel));
                                    } else {
                                        // primitive types: String, Int...
                                        response.put("model", genericReturnClass.getSimpleName().toLowerCase());
                                    }
                                }
                            } catch (SecurityException securityException) {
                                // System.out.println(securityException.getMessage());
                            }
                        } else {
                            try {
                                Class<?> returnClass = Class.forName(genericType.getTypeName());
                                RecordComponent[] recordComponents = returnClass.getRecordComponents();
                                
                                if (recordComponents != null) {
                                    // record objects - DTO
                                    
                                    JSONObject returnModel = new JSONObject();

                                    for (RecordComponent recordComponent : recordComponents) {
                                        returnModel.put(recordComponent.getName(), recordComponent.getType().getSimpleName().toLowerCase());
                                    }

                                    response.put("model", returnModel);
                                } else {
                                    // primitive types: String, Int...
                                    response.put("model", returnClass.getSimpleName().toLowerCase());
                                }
                            } catch (SecurityException securityException) {
                                // System.out.println(securityException.getMessage());
                            }
                        }
                    }
                }
                
                responses.add(response);

                /*** ENDPOINT METHOD AND ACTION ***/

                try {
                    GetMapping getMappingAnnotation = method.getAnnotation(GetMapping.class);
                    String[] getMappingAnnotationValues = getMappingAnnotation.value();
                    
                    for (String serviceEndpoint : serviceEndpoints) {
                        if (getMappingAnnotationValues.length > 0) {
                            for (String methodEndpoint : getMappingAnnotationValues) {
                                HashMap<String, Object> endpoint = new HashMap<>();
                                endpoint.put("method", "GET");
                                endpoint.put("action", ("/" + serviceEndpoint + "/" + methodEndpoint).replace("//", "/"));
                                endpoint.put("params", parameters);
                                endpoint.put("responses", responses);
                                endpoints.add(endpoint);
                            }
                        } else {
                            HashMap<String, Object> endpoint = new HashMap<>();
                            endpoint.put("method", "GET");
                            endpoint.put("action", ("/" + serviceEndpoint).replace("//", "/"));
                            endpoint.put("params", parameters);
                            endpoint.put("responses", responses);
                            endpoints.add(endpoint);
                        }
                    }
                } catch (NullPointerException exception) {
                    // System.out.println(exception.getMessage());
                }
                try {
                    PostMapping postMappingAnnotation = method.getAnnotation(PostMapping.class);
                    String[] postMappingAnnotationValues = postMappingAnnotation.value();
                    
                    for (String serviceEndpoint : serviceEndpoints) {
                        if (postMappingAnnotationValues.length > 0) {
                            for (String methodEndpoint : postMappingAnnotationValues) {
                                HashMap<String, Object> endpoint = new HashMap<>();
                                endpoint.put("method", "POST");
                                endpoint.put("action", ("/" + serviceEndpoint + "/" + methodEndpoint).replace("//", "/"));
                                endpoint.put("params", parameters);
                                endpoint.put("responses", responses);
                                endpoints.add(endpoint);
                            }
                        } else {
                            HashMap<String, Object> endpoint = new HashMap<>();
                            endpoint.put("method", "POST");
                            endpoint.put("action", ("/" + serviceEndpoint).replace("//", "/"));
                            endpoint.put("params", parameters);
                            endpoint.put("responses", responses);
                            endpoints.add(endpoint);
                        }
                    }
                } catch (NullPointerException exception) {
                    // System.out.println(exception.getMessage());
                }

                controller.put("endpoints", endpoints);
            }

            controllers.add(controller);
            service.put("controllers", controllers);
            services.add(service);

        } catch (NullPointerException nullPointerException) {
            // System.out.println(nullPointerException.getMessage());
        } catch (ClassNotFoundException classNotFoundException) {
            // System.out.println(classNotFoundException.getMessage());
        }

        model.addAttribute("services", services);

        return "index";
    }
}