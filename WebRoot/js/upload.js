
imageSizeRatio = 1.3333333;
thumbImageLargeSideMaxSize = 120;
selectedImageId = 0;
origImageWidth = new Array();
origImageHeight = new Array();

function isImageOk(img) 
{
    if (!img.complete) return false;
    if (typeof img.naturalWidth != "undefined" && img.naturalWidth == 0) return false;
    return true;
}

function resizeImage(image,imageLargeSideMaxSize)
{	
	imageSmallSideMaxSize = imageLargeSideMaxSize / imageSizeRatio;
	imageWidth = image.width;
	imageHeight = image.height;
	if (((imageWidth/imageHeight) >= imageSizeRatio) && (imageWidth > imageLargeSideMaxSize))
	{
		image.width = imageLargeSideMaxSize;
		image.height = imageLargeSideMaxSize * imageHeight/imageWidth;
	} else if (((imageWidth/imageHeight) < imageSizeRatio) && (imageHeight > imageSmallSideMaxSize))
	{
		image.height = imageSmallSideMaxSize;
		image.width = imageSmallSideMaxSize * imageWidth/imageHeight;
	} 	
}

function resizeThumbImages()
{
	pageImageNum = 0;
	while (image=document.getElementById('pageImage'+String(pageImageNum)))
	{
		origImageWidth['pageImage'+pageImageNum] = image.width;
		origImageHeight['pageImage'+pageImageNum] = image.height;
		resizeImage(image,thumbImageLargeSideMaxSize);
        if (isImageOk(image)) 
        	image.style.display='inline';
        	else image.style.display='none';
		pageImageNum++;		
	}
}

function selectImage(thumbImage)
{
	if (null != thumbImage) 
	{
		selectedImage=document.getElementById('selectedImage');
		selectedImage.src = thumbImage.src;
		selectedImage.width = origImageWidth[thumbImage.id];
		selectedImage.height = origImageHeight[thumbImage.id];
		resizeImage(selectedImage,380);
		document.upload.selectedImagesUrls.value = selectedImage.src;
	}
}

function showMoreImageUploadLines()
{
	additionalImages=document.getElementById('additionalImagesDiv');
	if (additionalImages) additionalImages.style.display='block';
	additionalImagesLink=document.getElementById('additionalImagesLinkDiv');
	if (additionalImagesLink) additionalImagesLink.style.display='none';	
}

function resetForm(imageId)
{
	resizeThumbImages();
	selectedImageId = imageId;
	selectImage(document.getElementById('pageImage'+String(imageId)));
}

function toggleNewProject(o,newProjectValue)
{
	var newProjectDiv = document.getElementById('newProjectDiv');
	if (o.value == newProjectValue) 
		newProjectDiv.style.display='block'; 
		else newProjectDiv.style.display='none'; 
}

