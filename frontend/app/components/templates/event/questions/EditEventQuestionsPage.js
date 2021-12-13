import React, { useCallback, useEffect, useState } from 'react';
import { Controller, useFieldArray, useForm } from 'react-hook-form';
import { FaPlus, FaTextWidth, FaTrash } from 'react-icons/fa';
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import Button from '../../../elements/Button/Button';
import { BsChatText } from 'react-icons/bs';
import EventApi from '../../../../api/event.api';
import { toast } from 'react-toastify';
import useActiveEvent from '../../../../hooks/useActiveEvent';
import { useRouter } from 'next/router';
import LoadingIndicator from '../../../elements/LoadingIndicator/LoadingIndicator';

const NewQuestionButton = ({ onClick }) => {
  return (
    <button
      type="button"
      className="w-full mb-4 rounded border-2 border-dotted py-5 flex flex-col gap-y-2 items-center text-gray-500 hover:bg-gray-100 transition text-sm"
      onClick={onClick}
    >
      <div className="border-2 border-dotted rounded-3xl p-1.5">
        <FaPlus color="gray" size="1rem" />
      </div>
      <span>New Question</span>
    </button>
  );
};

const QuestionTypeOption = ({ name, control, value, setValue, children }) => {
  return (
    <Controller
      name={name}
      control={control}
      defaultValue="SHORT_TEXT"
      render={({ field }) => (
        <button
          type="button"
          onClick={() => setValue(name, value)}
          className={`${
            value === field.value
              ? 'border-yellow-400 text-yellow-400 hover:bg-yellow-400 hover:text-white'
              : 'text-gray-400 hover:bg-gray-100'
          } border p-2 bg-white text-sm font-semibold transition rounded`}
        >
          {children}
        </button>
      )}
    />
  );
};

const IsRequiredSwitch = ({ name, control }) => {
  return (
    <Controller
      name={name}
      control={control}
      defaultValue={false}
      render={({ field }) => (
        <label className="flex flex-col gap-1">
          <input
            type="checkbox"
            className="hidden"
            checked={field.value}
            onChange={field.onChange}
          />
          <div
            className={`relative p-1 ${
              field.value ? 'bg-blue-200' : 'bg-gray-200'
            } rounded-3xl w-12 h-6 transition`}
          >
            <div
              className={`absolute top-1 bottom-1 ${
                field.value ? 'translate-x-6' : 'translate-x-0'
              } h-4 w-4 bg-white rounded-full transition`}
            />
          </div>
        </label>
      )}
    />
  );
};

const Question = ({ index, control, register, setValue, remove }) => (
  <div className="group px-4 py-6 mb-4 bg-white rounded border-2 border-dotted w-full relative flex flex-col space-y-4">
    <input
      className="font-medium outline-none placeholder-gray-500"
      placeholder="Click here to add title *"
      type="text"
      {...register(`questions[${index}].title`)}
    />
    <input
      className="text-sm outline-none placeholder-gray-500"
      placeholder="Click here to add description"
      type="text"
      {...register(`questions[${index}].description`)}
    />
    <div className="flex flex-col gap-1">
      <span className="text-gray-400 text-xs">QUESTION TYPE</span>
      <div className="flex gap-2">
        <QuestionTypeOption
          control={control}
          name={`questions[${index}].type`}
          value="SHORT_TEXT"
          setValue={setValue}
        >
          <div className="flex items-center space-x-1">
            <FaTextWidth />
            <span>Short text</span>
          </div>
        </QuestionTypeOption>
        <QuestionTypeOption
          control={control}
          name={`questions[${index}].type`}
          value="LONG_TEXT"
          setValue={setValue}
        >
          <div className="flex items-center space-x-1">
            <BsChatText />
            <span>Long text</span>
          </div>
        </QuestionTypeOption>
      </div>
    </div>
    <div className="flex flex-row justify-between items-end">
      <div className="flex flex-col gap-1">
        <span className="text-xs text-gray-400">IS REQUIRED?</span>
        <IsRequiredSwitch
          control={control}
          name={`questions[${index}].required`}
        />
      </div>
      <button
        type="button"
        className="shadow p-1 invisible group-hover:visible opacity-50 hover:opacity-90"
        onClick={() => remove(index)}
      >
        <FaTrash color="red" />
      </button>
    </div>
  </div>
);

const EditEventQuestionsPage = () => {
  const router = useRouter();
  const { event, slug, reload } = useActiveEvent();
  const [loading, setLoading] = useState(true);

  const { control, register, setValue, handleSubmit } = useForm();
  const { fields, append, remove, move } = useFieldArray({
    name: 'questions',
    control: control,
  });

  const populateForm = useCallback(
    (questions) => {
      setValue(
        'questions',
        questions.map((question) => ({
          questionId: question.id,
          title: question.title,
          description: question.description,
          type: question.type,
          required: question.required,
          order: question.order,
        }))
      );
    },
    [setValue]
  );

  useEffect(() => {
    if (event) {
      if (!event.organizing) {
        router.push(`/event/${slug}`);
        return;
      }
      populateForm(event.questions);
      setLoading(false);
    }
  }, [event, router, slug, populateForm]);

  const onSubmit = (data) => {
    const questions = data.questions;
    questions.forEach((question, index) => {
      question.order = index + 1;
      question.id = question.questionId ?? null;
    });

    EventApi.updateEventQuestions({ slug, questions })
      .then((res) => {
        toast.success('Questions updated successfully!');
        populateForm(res.data.data);
        reload();
      })
      .catch((err) => {
        toast.error(err.response.data.message);
      });
  };

  const onDragEnd = (result) => {
    const { destination, source } = result;
    if (destination) move(source.index, destination.index);
  };

  return loading ? (
    <LoadingIndicator isLoading={loading} />
  ) : (
    <form onSubmit={handleSubmit(onSubmit)}>
      <DragDropContext onDragEnd={onDragEnd}>
        <div className="flex flex-col">
          <Droppable droppableId="droppable">
            {(provided) => (
              <div {...provided.droppableProps} ref={provided.innerRef}>
                {fields.map((item, index) => {
                  return (
                    <Draggable
                      key={item.id}
                      draggableId={item.id}
                      index={index}
                    >
                      {(provided) => (
                        <div
                          {...provided.draggableProps}
                          {...provided.dragHandleProps}
                          ref={provided.innerRef}
                          className="flex flex-col gap-4"
                        >
                          <Question
                            index={index}
                            control={control}
                            register={register}
                            setValue={setValue}
                            remove={remove}
                          />
                        </div>
                      )}
                    </Draggable>
                  );
                })}
                {provided.placeholder}
              </div>
            )}
          </Droppable>
          <NewQuestionButton
            onClick={() =>
              append({
                title: '',
                description: '',
                type: 'SHORT_TEXT',
                required: false,
              })
            }
          />
          <div className="self-end">
            <Button appearance="success" size="lg">
              Save
            </Button>
          </div>
        </div>
      </DragDropContext>
    </form>
  );
};

export default EditEventQuestionsPage;
