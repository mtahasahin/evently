import React from 'react';
import Modal from 'react-modal';
import styles from './Modal.module.css'

const CustomModal = ({isOpen, setOpen, children}) => {
    return (
        <Modal isOpen={isOpen} className={styles.content} overlayClassName={styles.overlay}
               onRequestClose={() => setOpen(false)}>
            {children}
        </Modal>
    );
};

export default CustomModal;