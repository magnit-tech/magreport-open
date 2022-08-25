import React from "react";
import {Container, Toolbar, Typography} from "@material-ui/core";
import {ViewerCSS} from "./ViewerCSS";

/**
 * Компонент-контейнер для карточек дочерних объектов (ViewerChildCard) просмотрщика объектов
 * @param {Object} props - параметры компонента
 * @param {String} props.header - заголовок контейнера
 * @param {Array.<ViewerChildCard>} props.children - массив карточек
 * @return {JSX.Element}
 * @constructor
 */
export default function ViewerChildrenPanel(props) {
    const classes = ViewerCSS();

    return (
        <div>
            <Toolbar className={classes.viewerChildrenPanelTitleBar}>
                <Typography variant="h6">{props.header}</Typography>
            </Toolbar>
            <Container className={classes.viewerChildrenPanelContainer}>
                {props.children}
            </Container>
        </div>
    );
}
