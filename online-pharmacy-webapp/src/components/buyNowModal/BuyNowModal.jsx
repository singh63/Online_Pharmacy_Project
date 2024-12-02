/* eslint-disable react/prop-types */
import {useState} from "react";
import {Button, Modal} from "react-bootstrap";

const BuyNowModal = ({buyNowFunction}) => {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
            <Button variant="primary" onClick={handleShow} className="w-100">
                Buy now
            </Button>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Make payment</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <img src="/qr.png" alt="QR for payment" className="img-fluid"/>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button
                        variant="primary"
                        onClick={() => {
                            handleClose();
                            buyNowFunction();
                        }}
                    >
                        Buy now
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default BuyNowModal;
