export default function Upload({setUpload, userId, setCurrentProfil}) {
	
	const e = React.createElement
	const formRef = React.useRef(null);
	
	function updateAction() {
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/upload")
			.then((response) => {
				return response.text();
			})
			.then((imageUploadUrl) => {
				formRef.current.action = imageUploadUrl;
			});
	}
	
	React.useEffect(() => {
		updateAction()
	}, [formRef.current]);
	
	function handleUpload() {
		updateAction();
		alert("Photo upload !");
	}
	
	return (
		e("div", { className: "flex w-full justify-center" },
			e("iframe", {
			  name: "noredirect",
			  id: "noredirect",
			  style: {display: "none"}
			}),
			e("form", {
			  id: "createPostForm",
			  method: "post",
			  encType: "multipart/form-data",
			  target: "noredirect",
			  ref: formRef
			}, e("input", {
			  type: "file",
			  name: "img"
			}), e("input", {
			  type: "submit",
			  value: "Valider",
			  className: "bg-blue-200 rounded-xl px-2 pt-1 text-white cursor-pointer",
			  onClick: updateAction
			}))
		)
	)
}