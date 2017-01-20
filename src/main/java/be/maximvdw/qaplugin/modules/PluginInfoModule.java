package be.maximvdw.qaplugin.modules;

import be.maximvdw.qaplugin.api.*;
import be.maximvdw.qaplugin.api.ai.*;
import be.maximvdw.qaplugin.api.annotations.*;
import be.maximvdw.qaplugin.api.exceptions.FeatureNotEnabled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * PluginInfoModule
 * <p>
 * Created by maxim on 26-Dec-16.
 */
@ModuleName("PluginInfo")
@ModuleActionName("plugininfo")
@ModuleAuthor("Maximvdw")
@ModuleDescription("Ask information about the plugins on this server")
@ModuleVersion("1.1.0")
@ModuleConstraints({
        @ModuleConstraint(type = ModuleConstraint.ContraintType.QAPLUGIN_VERSION, value = "1.9.0")
})
@ModuleScreenshots({
        "http://i.mvdw-software.com/2016-12-27_00-47-15.png"
})
public class PluginInfoModule extends AIModule {

    public PluginInfoModule() {

    }

    @Override
    public void onEnable() {
        // Create custom entities with dynamic data
        // This will make the matching of the author/plugin names more easy
        Entity pluginAuthorsEntity = new Entity("plugin-authors");
        Entity pluginsEntity = new Entity("server-plugins");
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            // To the plugins entity add all the plugin names
            pluginsEntity.addEntry(new EntityEntry(pl.getName()));
            for (String author : pl.getDescription().getAuthors()) {
                // Add all the authors of each plugin to the plugin-authors entity
                pluginAuthorsEntity.addEntry(new EntityEntry(author));
            }
        }

        Intent pluginInfoAll = new Intent("QAPlugin-module-plugininfo-all")
                .addTemplate("what plugins do you have?")
                .addTemplate("what types of plugins does this server have?")
                .addTemplate("can you give me a list of plugins?")
                .addTemplate("what plugins does the server have?")
                .addTemplate("provide a list of plugins")
                .addResponse(new IntentResponse()
                        .withAction(this)
                        .addParameter(new IntentResponse.ResponseParameter("question", "ALL")));
        Intent pluginInfoCount = new Intent("QAPlugin-module-plugininfo-count")
                .addTemplate("how many plugins are there")
                .addTemplate("how many plugins does this server have?")
                .addTemplate(new IntentTemplate()
                        .addPart("how many plugins did ")
                        .addPart("maximvdw", "author", pluginAuthorsEntity)
                        .addPart(" make?"))
                .addTemplate(new IntentTemplate()
                        .addPart("how many plugins of those are made by ")
                        .addPart("maximvdw", "author", pluginAuthorsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("how many did ")
                        .addPart("maximvdw", "author", pluginAuthorsEntity)
                        .addPart(" make?"))
                .addTemplate(new IntentTemplate()
                        .addPart("what is the plugin count for ")
                        .addPart("maximvdw", "author", pluginAuthorsEntity)
                        .addPart(" plugins?"))
                .addResponse(new IntentResponse()
                        .withAction(this)
                        .addAffectedContext(new Context("plugininfo", 1))
                        .addParameter(new IntentResponse.ResponseParameter("question", "PLUGIN_COUNT"))
                        .addParameter(new IntentResponse.ResponseParameter("author", "$author")
                                .withDataType(pluginAuthorsEntity)
                                .withDefaultValue("#plugininfo.author")));
        Intent pluginInfoVersion = new Intent("QAPlugin-module-plugininfo-version")
                .addTemplate(new IntentTemplate()
                        .addPart("what is the version of ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("what version is used for ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("what version are you using for ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addResponse(new IntentResponse()
                        .withAction(this)
                        .addAffectedContext(new Context("plugininfo", 1))
                        .addParameter(new IntentResponse.ResponseParameter("question", "VERSION"))
                        .addParameter(new IntentResponse.ResponseParameter("plugin", "$plugin")
                                .setRequired(true)
                                .withDataType(pluginsEntity)
                                .withDefaultValue("#plugininfo.plugin")
                                .addPrompt("For what plugin do you want to know the version?")
                                .addPrompt("What is the name of the plugin you want to know the version of?")
                                .addPrompt("Can you tell me the name of the plugin?")
                                .addPrompt("What plugin?")));
        Intent pluginInfoAuthor = new Intent("QAPlugin-module-plugininfo-author")
                .addTemplate("who made it?")
                .addTemplate(new IntentTemplate()
                        .addPart("what is the author of ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("who made ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("who is the owner of ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("who is the creator of ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addResponse(new IntentResponse()
                        .withAction(this)
                        .addAffectedContext(new Context("plugininfo", 1))
                        .addParameter(new IntentResponse.ResponseParameter("question", "AUTHOR"))
                        .addParameter(new IntentResponse.ResponseParameter("plugin", "$plugin")
                                .setRequired(true)
                                .withDataType(pluginsEntity)
                                .withDefaultValue("#plugininfo.plugin")
                                .addPrompt("For what plugin do you want to know the author?")
                                .addPrompt("What is the name of the plugin you want to know the author of?")
                                .addPrompt("Can you tell me the name of the plugin?")
                                .addPrompt("What plugin?")));
        Intent pluginInfoSite = new Intent("QAPlugin-module-plugininfo-site")
                .addTemplate(new IntentTemplate()
                        .addPart("what is the site of ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("where can I get more info about ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("what is the website of ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("where can I find the site for ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addResponse(new IntentResponse()
                        .withAction(this)
                        .addAffectedContext(new Context("plugininfo", 1))
                        .addParameter(new IntentResponse.ResponseParameter("question", "SITE"))
                        .addParameter(new IntentResponse.ResponseParameter("plugin", "$plugin")
                                .setRequired(true)
                                .withDataType(pluginsEntity)
                                .withDefaultValue("#plugininfo.plugin")
                                .addPrompt("For what plugin do you want to know the site?")
                                .addPrompt("What is the name of the plugin you want to know the site of?")
                                .addPrompt("Can you tell me the name of the plugin?")
                                .addPrompt("What plugin?")));
        Intent pluginInfoDescription = new Intent("QAPlugin-module-plugininfo-description")
                .addTemplate("what does it do")
                .addTemplate(new IntentTemplate()
                        .addPart("tell me about ")
                        .addPart("featherboard", "plugin", pluginsEntity))
                .addTemplate(new IntentTemplate()
                        .addPart("what is the description of ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("can you tell me more about ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("what is the purpose of ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addTemplate(new IntentTemplate()
                        .addPart("for what can I use ")
                        .addPart("featherboard", "plugin", pluginsEntity)
                        .addPart(" ?"))
                .addResponse(new IntentResponse()
                        .withAction(this)
                        .addAffectedContext(new Context("plugininfo", 1))
                        .addParameter(new IntentResponse.ResponseParameter("question", "DESCRIPTION"))
                        .addParameter(new IntentResponse.ResponseParameter("plugin", "$plugin")
                                .setRequired(true)
                                .withDataType(pluginsEntity)
                                .withDefaultValue("#plugininfo.plugin")
                                .addPrompt("For what plugin do you want to know the description?")
                                .addPrompt("What is the name of the plugin you want to know the description of?")
                                .addPrompt("Can you tell me the name of the plugin?")
                                .addPrompt("What plugin?")
                                .addPrompt("So, for what plugin do you want to know more about?")));
        try {
            // Upload the entities
            if (!QAPluginAPI.uploadEntity(pluginAuthorsEntity)) {
                warning("Unable to upload entity!");
            }
            if (!QAPluginAPI.uploadEntity(pluginsEntity)) {
                warning("Unable to upload entity!");
            }

            // Upload the intents
            if (!QAPluginAPI.uploadIntent(pluginInfoAll)) {
                warning("Unable to upload intent!");
            }
            if (!QAPluginAPI.uploadIntent(pluginInfoAuthor)) {
                warning("Unable to upload intent!");
            }
            if (!QAPluginAPI.uploadIntent(pluginInfoDescription)) {
                warning("Unable to upload intent!");
            }
            if (!QAPluginAPI.uploadIntent(pluginInfoCount)) {
                warning("Unable to upload intent!");
            }
            if (!QAPluginAPI.uploadIntent(pluginInfoSite)) {
                warning("Unable to upload intent!");
            }
            if (!QAPluginAPI.uploadIntent(pluginInfoVersion)) {
                warning("Unable to upload intent!");
            }
        } catch (FeatureNotEnabled ex) {
            severe("You do not have a developer access token in your QAPlugin config!");
        }

        // Possible responses
        addResponse("plugin-all", "We use: ${plugins}");
        addResponse("plugin-all", "This is a list of our plugins: ${plugins}");

        addResponse("plugin-version", "The version of ${plugin} is ${version}");
        addResponse("plugin-version", "We use ${version} for ${plugin}");

        addResponse("plugin-author", "The author of ${plugin} is ${author}");
        addResponse("plugin-author", "${plugin}'s author is ${author}");

        addResponse("plugin-site", "The site for ${plugin} is ${site}");
        addResponse("plugin-site", "You can find more info about ${plugin} on ${site}");
        addResponse("plugin-site", "You can find more info about ${plugin} at ${site}");
        addResponse("plugin-site", "You can find more info here ${site}");
        addResponse("plugin-site", "You can see more info at: ${site}");

        addResponse("plugin-description", "The description for ${plugin} is ${description}");
        addResponse("plugin-description", "The description is ${description}");
        addResponse("plugin-description", "I quote \"${description}\"");

        addResponse("plugin-count", "There are ${count} plugins on this server");
        addResponse("plugin-count", "There are ${count} plugins here");
        addResponse("plugin-count", "We have a total of ${count} plugins on this server");
        addResponse("plugin-count", "We have a total of ${count} plugins here");
        addResponse("plugin-count", "We have a total of ${count}");

        addResponse("plugin-count-author", "There are ${count} plugins on this server made by ${author}");
        addResponse("plugin-count-author", "There are ${count} plugins on this server made by ${author} here");
        addResponse("plugin-count-author", "There are ${count} plugins on this server made by ${author} on this server");
        addResponse("plugin-count-author", "We are using ${count} plugins from ${author}");
        addResponse("plugin-count-author", "We are using ${count} plugins from ${author} on this server");
        addResponse("plugin-count-author", "We are using ${count} plugins from ${author} here");
        addResponse("plugin-count-author", "${count} plugins from ${author}");

        addErrorResponse("plugin-noauthor", "There is no author for this plugin :S");
        addErrorResponse("plugin-noauthor", "This plugin didn't configure an author!");
    }

    public String getResponse(AIQuestionEvent event) {
        Player player = event.getPlayer();

        Map<String, String> params = event.getParameters();
        if (!params.containsKey("question")) {
            return event.getDefaultResponse();
        }

        // Get the question type from the parameter
        QuestionType questionType = QuestionType.valueOf(params.get("question").toUpperCase());
        switch (questionType) {
            case ALL:
                String pluginStr = Bukkit.getPluginManager().getPlugins()[0].getName();
                for (int i = 1; i < Bukkit.getPluginManager().getPlugins().length; i++) {
                    pluginStr += ", " + Bukkit.getPluginManager().getPlugins()[i].getName();
                }
                Map<String, String> placeholders = new HashMap<String, String>();
                placeholders.put("plugins", pluginStr);
                return getRandomResponse("plugin-all", placeholders, player);
            case PLUGIN_COUNT:
                if (params.containsKey("author")) {
                    int count = 0;
                    for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
                        for (String name : pl.getDescription().getAuthors()) {
                            if (name.equalsIgnoreCase(params.get("author"))) {
                                count++;
                                break;
                            }
                        }
                    }
                    placeholders = new HashMap<String, String>();
                    placeholders.put("count", String.valueOf(count));
                    placeholders.put("author", String.valueOf(params.get("author")));
                    return getRandomResponse("plugin-count-author", placeholders, player);
                } else {
                    placeholders = new HashMap<String, String>();
                    placeholders.put("count", String.valueOf(Bukkit.getPluginManager().getPlugins().length));
                    return getRandomResponse("plugin-count", placeholders, player);
                }
            case DESCRIPTION:
                if (params.containsKey("plugin")) {
                    Plugin pl = Bukkit.getPluginManager().getPlugin(params.get("plugin"));
                    placeholders = new HashMap<String, String>();
                    placeholders.put("plugin", pl.getName());
                    placeholders.put("description", pl.getDescription().getDescription());
                    return getRandomResponse("plugin-author", placeholders, player);
                } else {
                    return event.getDefaultResponse();
                }
            case SITE:
                if (params.containsKey("plugin")) {
                    Plugin pl = Bukkit.getPluginManager().getPlugin(params.get("plugin"));
                    placeholders = new HashMap<String, String>();
                    placeholders.put("plugin", pl.getName());
                    placeholders.put("site", pl.getDescription().getWebsite());
                    return getRandomResponse("plugin-site", placeholders, player);
                } else {
                    return event.getDefaultResponse();
                }
            case AUTHOR:
                if (params.containsKey("plugin")) {
                    Plugin pl = Bukkit.getPluginManager().getPlugin(params.get("plugin"));
                    placeholders = new HashMap<String, String>();
                    placeholders.put("plugin", pl.getName());
                    if (pl.getDescription().getAuthors().size() == 0) {
                        return getRandomErrorResponse("plugin-noauthor", placeholders, player);
                    } else if (pl.getDescription().getAuthors().size() == 1) {
                        placeholders.put("author", pl.getDescription().getAuthors().get(0));
                    } else {
                        placeholders.put("author", pl.getDescription().getAuthors().get(0));
                    }
                    return getRandomResponse("plugin-author", placeholders, player);
                } else {
                    return event.getDefaultResponse();
                }
            case VERSION:
                if (params.containsKey("plugin")) {
                    Plugin pl = Bukkit.getPluginManager().getPlugin(params.get("plugin"));
                    placeholders = new HashMap<String, String>();
                    placeholders.put("plugin", pl.getName());
                    placeholders.put("version", pl.getDescription().getVersion());
                    return getRandomResponse("plugin-version", placeholders, player);
                } else {
                    return event.getDefaultResponse();
                }
        }
        return null;
    }

    enum QuestionType {
        ALL, PLUGIN_COUNT, DESCRIPTION, SITE, AUTHOR, VERSION
    }
}
