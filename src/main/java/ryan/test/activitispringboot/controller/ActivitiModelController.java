package ryan.test.activitispringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/model")
public class ActivitiModelController {

    @Autowired
    private RepositoryService repositoryService;
    /**
     * Activiti Modeler功能畫面進入點，進入時同時新增空白Model
     * @param request
     * @param response
     */
    @RequestMapping("/new")
    public void createModel(HttpServletRequest request, HttpServletResponse response) {
        String defaultModelName = "ModelName"; //Model初始化預設名稱
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Model modelData = repositoryService.newModel();
            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, defaultModelName);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "description");
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(defaultModelName);

            // 將Model資訊到ACT_RE_MODEL，取得Model ID
            repositoryService.saveModel(modelData);

            // 初始化Model對應之流程圖資訊(空白)
            ObjectNode editorNode = objectMapper.createObjectNode();
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            editorNode.set("stencilset", stencilSetNode);

            // 將流程圖資訊存入ACT_GE_BYTEARRAY
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

            // 進入Activiti Modeler功能畫面
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
