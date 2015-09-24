$(".options dt, .users dt").live "click", (e) ->
    e.preventDefault()
    if $(e.target).parent().hasClass("opened")
        $(e.target).parent().removeClass("opened")
    else
        $(e.target).parent().addClass("opened")
        $(document).one "click", ->
            $(e.target).parent().removeClass("opened")
    false

$.fn.editInPlace = (method, options...) ->
    this.each ->
        methods =
            # public methods
            init: (options) ->
                valid = (e) =>
                    newValue = @input.val()
                    options.onChange.call(options.context, newValue)
                cancel = (e) =>
                    this.$el.show()
                    @input.hide()
                this.$el = $(this).dblclick(methods.edit)
                @input = $("<input type=\"text\"></input>")
                    .insertBefore(this.$el)
                    .keyup (e) ->
                        switch(e.keyCode)
                            # Enter key
                            when 13 then $(this).blur()
                            # Escape key
                            when 27 then cancel(e)
                     .blur(valid)
                     .hide()
            edit: ->
                @input
                    .val(this.$el.text())
                    .show()
                    .focus()
                    .select()
                this.$el.hide()
            close: (newName) ->
                this.$el.text(newName).show()
                @input.hide()
        # jQuery approach: http://docs.jquery.com/Plugins/Authoring
        if ( methods[method] )
            return methods[ method ].apply(this, options)
        else if (typeof method == 'object')
            return methods.init.call(this, method)
        else
            $.error("Method " + method + " does not exist.")

class Drawer extends Backbone.View
    initialize: ->
        @$el.children("li").each (i,group) ->
            new Group
                el: $(group)
            $("li",group).each (i,project) ->
                new Project
                    el: $(project)
    addGroup: ->
        jsRoutes.controllers.Projects.addGroup().ajax
            success: (data) ->
                _view = new Group
                    el: $(data).appendTo("#projects")                

class Group extends Backbone.View
    events:
        "click    .toggle"          : "toggle"
        "click    .newProject"      : "newProject"
    newProject: (e) ->
        this.$el.removeClass("closed")
        jsRoutes.controllers.Projects.add().ajax
                context: this
                data:
                    group: this.$el.attr("data-group")
                success: (tpl) ->
                    _list = $("ul",this.$el)
                    _view = new Project
                        el: $(tpl).appendTo(_list)
                    _view.$el.find(".name").editInPlace("edit")
                error: (err) ->
                    $.error("Error: " + err)


class Project extends Backbone.View
    initialize: ->
        @id = this.$el.attr("data-project")
        @name = $(".name", this.$el).editInPlace
            context: this
            onChange: @renameProject
    renameProject: (name) ->
        @loading(true)
        jsRoutes.controllers.Projects.rename(@id).ajax
            context: this
            data:
                name: name
            success: (data) ->
                @loading(false)
                @name.editInPlace("close", data)
            error: (err) ->
                @loading(false)
                $.error("Error: " + err)
    loading: (display) ->
        if (display)
            this.$el.children(".delete").hide()
            this.$el.children(".loader").show()
        else
            this.$el.children(".delete").show()
            this.$el.children(".loader").hide()
    events:
        "click      .delete"        : "deleteProject"
    deleteProject: (e) ->
        @loading(true)
        e.preventDefault()
        jsRoutes.controllers.Projects.delete(@id).ajax
            context: this
            success: ->
                this.$el.remove()
                @loading(false)
            error: (err) ->
                @loading(false)
                $.error("Error: " + err)
        false

$ ->
    drawer = new Drawer el: $("#projects")     