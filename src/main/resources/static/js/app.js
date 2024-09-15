$(function () {
    //Make post form on top width overlay
    var $overlay = $('<div id = "overlay"></div>');
    var $post_form = $('#post-form');
    var $post_form_editor = $('#post-form-editor');
    var $post_form_close_button = $('#post-form-close-button');

    var removeOnTop = function () {
        $overlay.hide();
        $post_form_close_button.hide();
        // force lose focus
        $post_form_editor.blur();
        $post_form.removeClass('on-top');
    };

    $post_form_editor.focus(function () {
        $overlay.toggle();
        $('body').append($overlay);

        $post_form.addClass('on-top');
        $post_form_close_button.show();

        // Only use this as improved version
        $('#post-form-close-button, #overlay').on('click', removeOnTop);

        // ALso hide overlay when user scroll the window
        $(window).scroll(removeOnTop);
    });
    // End

    //create place holder text
    var $editable = $('[contenteditable]');
    $editable.html("<p class='place-holder-text'>What's on your mind?</p>");
    $editable.focus(function () {
        $(this).children('p').remove();
    });

    $editable.blur(function () {
        var content = $(this).html();
        if (content.length === 0) {
            $(this).html("<p class='place-holder-text'>What's on your mind?</p>");
        }
    });
    // End 

    var $friendDetailPopup;
    var $backTriangle;
    var $frontTriangle;
    var currentTop;
    var $backTriangleInitPos = 14;
    var $frontTriangleInitPos = 15;
    var limit = 100;
    $(document).on("mouseenter", "#online-list li", function () {
        $friendDetailPopup = $('#friend-detail-popup');
        $backTriangle = $('#back-triange');
        $frontTriangle = $('#front-triange');
        if ($(this).position().top >= $(window).height() - limit) {
            $friendDetailPopup.hide();
            return;
        }

        // Re-position of the popup so that it is not covered when reaching the bottom
        var popupTop = $(this).position().top; // need to get the top of the current hover item
        // calculate the bottom of the popup
        // We need the 'limit' to make sure we will not reach to close to the window's bottom
        var popupBottom = $friendDetailPopup.height() + popupTop + limit;

        // if the popup reaches the bottom of the window, move the triangle at delta size
        if (popupBottom >= $(window).height()) {
            var delta = Math.round($friendDetailPopup.height() / 2);
            currentTop = $(this).position().top - delta;
            $backTriangle.css({
                top: ($backTriangleInitPos + delta) + 'px'
            });
            $frontTriangle.css({
                top: ($frontTriangleInitPos + delta) + 'px'
            });
        } else { 
            // Need to use position() to get the pos of the current element relative to the parent (#online-list)
            //            currentTop = $(this).position().top + 5; // 5 to minus from the top
            currentTop = popupTop + 5;
            $backTriangle.css({
                top: $backTriangleInitPos + 'px'
            });
            $frontTriangle.css({
                top: $frontTriangleInitPos + 'px'
            });
        }

        $friendDetailPopup.css({
            top: currentTop
        });
        $friendDetailPopup.show(200);

    });

    $(document).on('mouseleave', '#online-list ul, #friend-detail-popup', function () {
        setTimeout(function () {
            if (!$friendDetailPopup.is(':hover')) { // Check if the popup is NOT hover
                $friendDetailPopup.hide();
            }
        }, 1000);
    });

});
