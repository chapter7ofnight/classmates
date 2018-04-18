$(".mousedown").click(function () {
    $("html,body").animate({scrollTop: $("#photo-wall-wrapper").offset().top}, 1000);
});

//获取照片墙背后div的高度
function photoWallWrapperHeight() {
    $("#photo-wall-wrapper").css("height", $(".photo-cntent").height() + 300 + "px");
}

photoWallWrapperHeight();
$(window).resize(function () {
    photoWallWrapperHeight();
});

//往照片墙动态添加照片
var a = 0;  //四个一循环，判断添加photoWrapper7还是photoWrapper5
var b = 0;  //用于添加照片上的颜色，photoColor的脚标
var c = 1;  //photo-wrapper的id
var photoColor = ["#d95252", "#db53ad", "#53dbb3", "#dbbd53", "#5653db", "#db8453", "#db53a5", "#97db53"];

function addTag7(photoAddress) {
    var photoWrapper7 = $("<div></div>", {
        class: "col-sm-7 photo-wrapper",
        id: "photo" + c
    });
    var photoA = $("<a></a>", {href: "javascript:void (0)"}); //套在照片外的a标签
    var myPhoto = $("<div></div>", {
        class: "myphoto",
        style: "background-image: url("+photoAddress+");"
    });
    var photoColorDiv = $("<div></div>", {
        class: "photo-color",
        style: "background-color: " + photoColor[b] + ";"
    });
    $(".photo-cntent").append(photoWrapper7.append(photoA.append(myPhoto.append(photoColorDiv))));
}

function addTag5(photoAddress) {
    var photoWrapper5 = $("<div></div>", {
        class: "col-sm-5 photo-wrapper",
        id: "photo" + c
    });
    var photoA = $("<a></a>", {href: "javascript:void (0)"}); //套在照片外的a标签
    var myPhoto = $("<div></div>", {
        class: "myphoto",
        style: "background-image: url("+photoAddress+");"
    });
    var photoColorDiv = $("<div></div>", {
        class: "photo-color",
        style: "background-color: " + photoColor[b] + ";"
    });
    $(".photo-cntent").append(photoWrapper5.append(photoA.append(myPhoto.append(photoColorDiv))));
}

function addPhoto(photoAddress) {
    if (b < photoColor.length) {
        if (a === 0) {
            addTag7(photoAddress);
            a = 1;
        }
        else if (a === 1) {
            addTag5(photoAddress);
            a = 2;
        }
        else if (a === 2) {
            addTag5(photoAddress);
            a = 3;
        }
        else if (a === 3) {
            addTag7(photoAddress);
            a = 0;
        }
        b++;
        c++;
        if (b === photoColor.length) {
            b = 0;
        }
    }
}

//点击添加新的照片调用addPhoto()
$("#add-new-photo").click(function () {

});

function setPhoto(photoAddress) {
    addPhoto(photoAddress);
    $(".myphoto").click(function () {
        $(".show-wrapper").removeClass("hidden").addClass("show");
        var url=$(this).css('background-image');
        photoToLarge(url.split('"')[1].split('"')[0]);
    });
    photoWallWrapperHeight();
}

//图片放大全屏显示效果
function photoToLarge(photoAddress) {
    var largePhoto = $("#large-photo");

    var photoWidth = largePhoto.width();
    var photoHeight = largePhoto.height();
    largePhoto.attr("src",photoAddress);

    if (photoHeight <= $(window).height() || photoHeight > photoWidth) {
        largePhoto.css("height", 10 + "px");
        largePhoto.stop().animate({
            height: $(window).height() - 100
        }, 1100, "swing");
    }
    else {
        largePhoto.css("width", 10 + "px");
        largePhoto.stop().animate({
            width: $(window).width() - 200
        }, 1100, "swing");
    }
}

$("#photo-close").click(function () {
    $(".show-wrapper").removeClass("show").addClass("hidden");
});

$(function () {
    $.post("/classmate/photoWall",{"classmateId":$("#classmateId").val()},
        function (data) {
            if(data.statusCode===200){
                $("#classmate-name").text(data.extend.classmate.name);
                $("#classmate-shcool").text(data.extend.classmate.school+" "+data.extend.classmate.clazz);
                $("#classmate-description").text(data.extend.classmate.description);

                var photos=data.extend.classmate.photoWall.split("|");
                $.each(photos,function (index,item) {
                    setPhoto(item);
                });
            }
            else if(data.statusCode===400){
                alert(data.msg);
            }
            else{
                alert("网络错误，请稍后重试");
            }
        });
});