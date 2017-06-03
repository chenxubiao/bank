package cn.longhaiyan.test;


/**
 * Created by chenxb on 17-6-2.
 */
public class Test {

//    public Response s(String url) {
//        PostMethod post = new PostMethod(url);
//        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
//        try {
//            long t = System.currentTimeMillis();
//            Part[] parts = null;
//            if (params == null) {
//                parts = new Part[1];
//            } else {
//                parts = new Part[params.length + 1];
//            }
//            if (params != null) {
//                int i = 0;
//                for (PostParameter entry : params) {
//                    parts[i++] = new StringPart(entry.getName(),
//                            (String) entry.getValue(), "UTF-8");
//                }
//            }
//            FilePart filePart = new FilePart(fileParamName, file.getName(),
//                    file, "multipart/form-data", "UTF-8");
//            filePart.setTransferEncoding("binary");
//            parts[parts.length - 1] = filePart;
//
//            post.setRequestEntity(new MultipartRequestEntity(parts, post
//                    .getParams()));
//            List<Header> headers = new ArrayList<Header>();
//
//            client.getHostConfiguration().getParams()
//                    .setParameter("http.default-headers", headers);
//            client.executeMethod(post);
//
//            Response response = new Response();
//            response.setResponseAsString(post.getResponseBodyAsString());
//            response.setStatusCode(post.getStatusCode());
//
//            log("multPartURL URL:" + url + ", result:" + response + ", time:"
//                    + (System.currentTimeMillis() - t));
//            return response;
//        } catch (Exception ex) {
//            throw new SystemException(ex.getMessage(), ex, -1);
//        } finally {
//            post.releaseConnection();
//            client = null;
//        }
//    }
}
