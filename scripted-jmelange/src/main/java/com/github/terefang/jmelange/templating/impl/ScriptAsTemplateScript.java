package com.github.terefang.jmelange.templating.impl;

import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.VariableProvider;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import lombok.Data;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

@Data
public class ScriptAsTemplateScript extends AbstractTemplateScript
{
    String outputType = "text";
    AbstractScript script;

    public static ScriptAsTemplateScript from(AbstractScript _script)
    {
        ScriptAsTemplateScript _scp = new ScriptAsTemplateScript();
        _scp.setScript(_script);
        return _scp;
    }

    @Override
    public boolean init(Reader _script)
    {
        return this.initTemplate(_script);
    }

    public boolean initTemplate(Reader _script)
    {
        return this.script.init(_script);
    }

    @Override
    public Object executeObject()
    {
        return this.script.executeObject();
    }

    @Override
    public boolean executeTemplate()
    {
        return this.script.execute();
    }

    @Override
    public List<Object> getArgs() {
        return script.getArgs();
    }

    @Override
    public File getDestinationFile() {
        return script.getDestinationFile();
    }

    @Override
    public Logger getErrorLogger() {
        return script.getErrorLogger();
    }

    @Override
    public List<File> getIncludeDirectories() {
        return script.getIncludeDirectories();
    }

    @Override
    public InputStream getInputStream() {
        return script.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return script.getOutputStream();
    }

    @Override
    public File getScriptFile() {
        return script.getScriptFile();
    }

    @Override
    public File getSourceFile() {
        return script.getSourceFile();
    }

    @Override
    public List<VariableProvider> getVariableProviders() {
        return script.getVariableProviders();
    }

    @Override
    public Map<String, Object> getVars() {
        return script.getVars();
    }

    @Override
    public void setArgs(List<Object> args) {
        script.setArgs(args);
    }

    @Override
    public void setDestinationFile(File destinationFile) {
        script.setDestinationFile(destinationFile);
    }

    @Override
    public void setErrorLogger(Logger errorLogger) {
        script.setErrorLogger(errorLogger);
    }

    @Override
    public void setIncludeDirectories(List<File> includeDirectories) {
        script.setIncludeDirectories(includeDirectories);
    }

    @Override
    public void setInputStream(InputStream inputStream) {
        script.setInputStream(inputStream);
    }

    @Override
    public void setOutputStream(OutputStream outputStream) {
        script.setOutputStream(outputStream);
    }

    @Override
    public void setScriptFile(File scriptFile) {
        script.setScriptFile(scriptFile);
    }

    @Override
    public void setSourceFile(File sourceFile) {
        script.setSourceFile(sourceFile);
    }

    @Override
    public void setVariableProviders(List<VariableProvider> variableProviders) {
        script.setVariableProviders(variableProviders);
    }

    @Override
    public void setVars(Map<String, Object> vars) {
        script.setVars(vars);
    }
}
