const {
	ClassicEditor,
	Alignment,
	AutoImage,
	AutoLink,
	Autosave,
	BlockQuote,
	Bold,
	CKBox,
	CloudServices,
	Emoji,
	Essentials,
	FontBackgroundColor,
	FontColor,
	FontFamily,
	FontSize,
	GeneralHtmlSupport,
	ImageBlock,
	ImageInsert,
	ImageInsertViaUrl,
	ImageResize,
	ImageStyle,
	ImageToolbar,
	ImageUpload,
	Italic,
	Link,
	MediaEmbed,
	Mention,
	Paragraph,
	PictureEditing,
	Strikethrough,
	Underline
} = window.CKEDITOR;

const LICENSE_KEY =
		'eyJhbGciOiJFUzI1NiJ9.eyJleHAiOjE3NDMyMDYzOTksImp0aSI6IjkwYjI5N2ZkLWRkMzktNGIyOS04NDE1LTM5MWQ1ZGViODgzOCIsInVzYWdlRW5kcG9pbnQiOiJodHRwczovL3Byb3h5LWV2ZW50LmNrZWRpdG9yLmNvbSIsImRpc3RyaWJ1dGlvbkNoYW5uZWwiOlsiY2xvdWQiLCJkcnVwYWwiLCJzaCJdLCJ3aGl0ZUxhYmVsIjp0cnVlLCJsaWNlbnNlVHlwZSI6InRyaWFsIiwiZmVhdHVyZXMiOlsiKiJdLCJ2YyI6ImRlMmNlY2IyIn0.39053QU8eA_NoEysgKgO0VIogla1cyTVonaZnTZWHhoXK0ira26V5H_b6mJGbmALdkRx3fV1Hk5Xo1DXHyqPYw';

const CLOUD_SERVICES_TOKEN_URL =
		'https://s37l0_wh2lwz.cke-cs.com/token/dev/5e10f3814168f56ddb5492186bf5f476a30eb16b0acde210a12c6aadb074?limit=10';

const editorConfig = {
	toolbar: {
		items: [
			'fontSize',
			'fontFamily',
			'fontColor',
			'fontBackgroundColor',
			'|',
			'bold',
			'italic',
			'underline',
			'strikethrough',
			'|',
			'emoji',
			'link',
			'insertImage',
			'ckbox',
			'mediaEmbed',
			'blockQuote',
			'|',
			'alignment'
		],
		shouldNotGroupWhenFull: false
	},
	plugins: [
		Alignment,
		AutoImage,
		AutoLink,
		Autosave,
		BlockQuote,
		Bold,
		CKBox,
		CloudServices,
		Emoji,
		Essentials,
		FontBackgroundColor,
		FontColor,
		FontFamily,
		FontSize,
		GeneralHtmlSupport,
		ImageBlock,
		ImageInsert,
		ImageInsertViaUrl,
		ImageResize,
		ImageStyle,
		ImageToolbar,
		ImageUpload,
		Italic,
		Link,
		MediaEmbed,
		Mention,
		Paragraph,
		PictureEditing,
		Strikethrough,
		Underline
	],
	cloudServices: {
		tokenUrl: CLOUD_SERVICES_TOKEN_URL
	},
	fontFamily: {
		supportAllValues: true
	},
	fontSize: {
		options: [10, 12, 14, 'default', 18, 20, 22],
		supportAllValues: true
	},
	htmlSupport: {
		allow: [
			{
				name: /^.*$/,
				styles: true,
				attributes: true,
				classes: true
			}
		]
	},
	image: {
		toolbar: [
			'imageTextAlternative',
			'|',
			'imageStyle:alignBlockLeft',
			'imageStyle:block',
			'imageStyle:alignBlockRight',
			'|',
			'resizeImage'
		],
		styles: {
			options: ['alignBlockLeft', 'block', 'alignBlockRight']
		},
		resizeUnit: '%', // % 단위로 크기 조절
		            resizeOptions: [
		                { name: 'resize50', value: '50', label: '50%' },
		                { name: 'resize75', value: '75', label: '75%' },
		                { name: 'resize100', value: '100', label: '100%' }
		            ]
	},
	licenseKey: LICENSE_KEY,
	link: {
		addTargetToExternalLinks: true,
		defaultProtocol: 'https://',
		decorators: {
			toggleDownloadable: {
				mode: 'manual',
				label: 'Downloadable',
				attributes: {
					download: 'file'
				}
			}
		}
	},
	mention: {
		feeds: [
			{
				marker: '@',
				feed: [
					/* See: https://ckeditor.com/docs/ckeditor5/latest/features/mentions.html */
				]
			}
		]
	},
	placeholder: 'Type or paste your content here!'
};

configUpdateAlert(editorConfig);

ClassicEditor.create(document.querySelector('#editor'), editorConfig);

/**
 * This function exists to remind you to update the config needed for premium features.
 * The function can be safely removed. Make sure to also remove call to this function when doing so.
 */
function configUpdateAlert(config) {
	if (configUpdateAlert.configUpdateAlertShown) {
		return;
	}

	const isModifiedByUser = (currentValue, forbiddenValue) => {
		if (currentValue === forbiddenValue) {
			return false;
		}

		if (currentValue === undefined) {
			return false;
		}

		return true;
	};

	const valuesToUpdate = [];

	configUpdateAlert.configUpdateAlertShown = true;

	if (!isModifiedByUser(config.cloudServices?.tokenUrl, '<YOUR_CLOUD_SERVICES_TOKEN_URL>')) {
		valuesToUpdate.push('CLOUD_SERVICES_TOKEN_URL');
	}

	if (valuesToUpdate.length) {
		window.alert(
			[
				'Please update the following values in your editor config',
				'to receive full access to Premium Features:',
				'',
				...valuesToUpdate.map(value => ` - ${value}`)
			].join('\n')
		);
	}
}
